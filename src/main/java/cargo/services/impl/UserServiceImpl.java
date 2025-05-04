package cargo.services.impl;
import cargo.dto.reponces.GetUserResponse;
import cargo.dto.requests.UpdateUserRequest;
import cargo.entity.Account;
import cargo.entity.User;
import cargo.entity.Role;
import cargo.exeptions.BadRequestException;
import cargo.exeptions.NotFoundException;
import cargo.repositories.AccountRepository;
import cargo.repositories.RoleRepository;
import cargo.repositories.UserRepository;
import cargo.dto.requests.RegisterWithRole;
import cargo.dto.requests.SignInRequest;
import cargo.services.JwtService;
import cargo.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cargo.dto.reponces.SignResponse;
import cargo.dto.requests.RegisterRequest;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final AccountRepository accountRepo;

    @PostConstruct
    public void initDefaultRoles() {
        if (roleRepo.findByNameIgnoreCase("USER").isEmpty()) {
            roleRepo.save(new Role(null, "USER", "Default role for regular users", true));
        }
    }


    @PostConstruct
    public void defaultAdmin() {
        boolean exists = userRepo.existsByEmail("admin@gmail.com");
        if (!exists) {
            Role adminRole = roleRepo.findByNameIgnoreCase("ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role(null, "ADMIN", "Default administrator role", true)));


            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("Adminov")
                    .email("admin@gmail.com")
                    .role(adminRole)
                    .build();

            Account adminAccount = Account.builder()
                    .login("admin")
                    .password(passwordEncoder.encode("Admin123"))
                    .isActive(true)
                    .user(adminUser)
                    .build();

            adminUser.setAccount(adminAccount);

            userRepo.save(adminUser);
        }
    }
    @Override
    public SignResponse signUp(RegisterRequest registerRequest) {
        if (userRepo.existsByEmail((registerRequest.email()))) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepo.findByNameIgnoreCase("USER")
                .orElseThrow(() -> new BadRequestException("Default role USER not found"));
        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .phoneNumber(registerRequest.phone())
                .email(registerRequest.email())
                .role(role)
                .build();

        Account account = Account.builder()
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .isActive(true)
                .user(user)
                .build();

        user.setAccount(account);

        User saveUser = userRepo.save(user);
        return SignResponse
                .builder()
                .id(user.getId())
                .token(jwtService.createJwtToken(saveUser))
                .email(saveUser.getEmail())
                .login(account.getLogin())
                .role(saveUser.getRole())
                .httpStatus(HttpStatus.OK)
                .message("Successfully ")
                .build();
    }

    @Override
    public SignResponse signIn(SignInRequest signInRequest) {
        Account account = accountRepo.findByLogin(signInRequest.login())
                .orElseThrow(() -> new BadRequestException("Account with login not found"));

        if (!passwordEncoder.matches(signInRequest.password(), account.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        User user = account.getUser();

        return SignResponse
                .builder()
                .id(user.getId())
                .token(jwtService.createJwtToken(user))
                .email(user.getEmail())
                .login(signInRequest.login())
                .role(user.getRole())
                .httpStatus(HttpStatus.OK)
                .message("Successfully ")
                .build();
    }

    @Override
    public SignResponse createUserByAdmin(RegisterWithRole registerRequest) {
        if (userRepo.existsByEmail(registerRequest.email())) {
            throw new BadRequestException("Email already exists");
        }
        if (registerRequest.role() == null || registerRequest.role().name().isBlank()) {
            throw new BadRequestException("Role is required for admin user creation");
        }

        String roleName = registerRequest.role().getClass().getName().toUpperCase();

        Role role = roleRepo.findByNameIgnoreCase(roleName).orElse(null);

        if (role == null) {
            role = new Role();
            role.setName(roleName);
            role.setDescription(registerRequest.role().description());
            role = roleRepo.save(role);
        }
        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .role(role)
                .build();

        Account account = Account.builder()
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .user(user)
                .isActive(true)
                .build();

        user.setAccount(account);

        User saveUser = userRepo.save(user);

        return SignResponse.builder()
                .id(saveUser.getId())
                .token(jwtService.createJwtToken(saveUser))
                .email(saveUser.getEmail())
                .role(saveUser.getRole())
                .httpStatus(HttpStatus.CREATED)
                .message("User created successfully by admin")
                .build();
    }

    @Override
    public Page<GetUserResponse> getAllUsers(Pageable pageable) {

        return userRepo.findAll(pageable)
                .map(user -> GetUserResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .role(user.getRole().getName())
                        .accountActive(user.getAccount().isActive())
                        .build());
    }

    @Override
    public GetUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.email() != null) user.setEmail(request.email());
        if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());

        if (request.roleName() != null) {
            Role role = roleRepo.findByNameIgnoreCase(request.roleName())
                    .orElseThrow(() -> new BadRequestException("Role not found"));
            user.setRole(role);
        }

        Account account = user.getAccount();
        if (account != null) {
            if (request.login() != null) account.setLogin(request.login());
            if (request.password() != null) account.setPassword(passwordEncoder.encode(request.password()));
            if (request.accountActive() != null) account.setActive(request.accountActive());
        }

        userRepo.save(user);

        return GetUserResponse.from(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepo.delete(user);
    }

}
