package cargo.services.impl;

import cargo.dto.reponces.GetClientResponse;
import cargo.dto.requests.CreateClientRequest;
import cargo.entity.Account;
import cargo.entity.Client;
import cargo.entity.Role;
import cargo.entity.User;
import cargo.exeptions.CustomException;
import cargo.exeptions.NotFoundException;
import cargo.repositories.AccountRepository;
import cargo.repositories.ClientRepository;
import cargo.repositories.RoleRepository;
import cargo.repositories.UserRepository;
import cargo.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepo;


    @Override
    public void createClient(CreateClientRequest request) {
        if (accountRepo.existsByLoginIgnoreCase(request.login())) {
            throw new CustomException("login exists!");
        }

        Role clientRole = roleRepo.findByNameIgnoreCase("CLIENT")
                .orElseThrow(() -> new NotFoundException("Role CLIENT not found"));

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .role(clientRole)
                .build();
        userRepo.save(user);

        Account account = Account.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .active(request.acStatus())
                .user(user)
                .build();
        accountRepo.save(account);

        Client client = Client.builder()
                .user(user)
                .country(request.country())
                .activityType(request.activityType())
                .address(request.address())
                .build();
        clientRepo.save(client);

    }

    @Override
    public void updateClient(Long clientId, CreateClientRequest request) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client with id " + clientId + " not found"));

        User user = client.getUser();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        userRepo.save(user);

        Account account = user.getAccount();
        if (account != null) {
            account.setLogin(request.login());
            account.setPassword(passwordEncoder.encode(request.password()));
            account.setActive(request.acStatus());
            accountRepo.save(account);
        }

        client.setCountry(request.country());
        client.setActivityType(request.activityType());
        client.setAddress(request.address());

        clientRepo.save(client);
    }

    @Override
    public void deleteClient(Long clientId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client with ID " + clientId + " not found"));

        User user = client.getUser();

        clientRepo.delete(client);

        if (user.getAccount() != null) {
            accountRepo.delete(user.getAccount());
        }

        userRepo.delete(user);
    }

    @Override
    public GetClientResponse getClientById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));

        User user = client.getUser();
        Account account = user.getAccount();

        return new GetClientResponse(
                client.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                account != null && account.isActive(),
                client.getCountry(),
                client.getActivityType(),
                client.getAddress(),
                account != null ? account.getLogin() : null
        );
    }

    public void deactivateClient(Long clientId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Account account = client.getUser().getAccount();
        if (account != null) {
            account.setActive(false);
            accountRepo.save(account);
        } else {
            throw new NotFoundException("Account not found for the client");
        }
    }
}
