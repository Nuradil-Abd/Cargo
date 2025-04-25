package cargo.services.impl;

import cargo.entity.User;
import cargo.entity.Role;
import cargo.exeptions.BadRequestException;
import cargo.repositories.RoleRepository;
import cargo.repositories.UserRepository;
import cargo.services.JwtService;
import cargo.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cargo.reponces.SignUpResponse;
import cargo.requests.RegisterRequest;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;



    @PostConstruct
    public void defaultAdmin() {
        boolean exists = userRepo.existsByEmail("admin@gmail.com");
        if (!exists) {
            Role adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role(null, "ADMIN", "Default administrator role")));

            userRepo.save(
                    User.builder()
                            .firstName("Admin")
                            .lastName("Adminov")
                            .email("admin@gmail.com")
                            .password(passwordEncoder.encode("Admin123"))
                            .role(adminRole)
                            .build());

        }
    }
    @Override
    public SignUpResponse signUp(RegisterRequest registerRequest) {
        if (userRepo.existsByEmail((registerRequest.email()))) {
            throw new BadRequestException("Email already exists");
        }

        String roleName = (registerRequest.role() == null || registerRequest.role().getName(). isBlank())
                ? "USER"
                : registerRequest.role().getName(). toUpperCase();

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("Role not found: " + roleName));

        User saveUser = userRepo.save(
                User.
                        builder()
                        .firstName(registerRequest.firstName())
                        .lastName(registerRequest.lastName())
                        .email(registerRequest.email())
                        .password(passwordEncoder.encode(registerRequest.password()))
                        .role(role)
                        .build());

        User user = userRepo.getUserByEmail(saveUser.getEmail());

        return SignUpResponse
                .builder()
                .id(user.getId())
                .token(jwtService.createJwtToken(saveUser))
                .email(saveUser.getEmail())
                .role(saveUser.getRole())
                .httpStatus(HttpStatus.OK)
                .message("Successfully ")
                .build();
    }

}
