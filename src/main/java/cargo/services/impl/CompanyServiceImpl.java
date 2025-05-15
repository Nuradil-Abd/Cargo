package cargo.services.impl;

import cargo.dto.requests.CreateCompanyRequest;
import cargo.dto.requests.CreateUserRequest;
import cargo.entity.Account;
import cargo.entity.Company;
import cargo.entity.Role;
import cargo.entity.User;
import cargo.exeptions.CustomException;
import cargo.exeptions.NotFoundException;
import cargo.repositories.AccountRepository;
import cargo.repositories.CompanyRepository;
import cargo.repositories.RoleRepository;
import cargo.repositories.UserRepository;
import cargo.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void create(CreateCompanyRequest request) {
        if (companyRepository.existsByTin(request.tin())) {
            throw new CustomException("Company with " + request.tin() + " TIN already exists!");
        }

        Company company = new Company();
        company.setName(request.name());
        company.setTin(request.tin());
        company.setAddress(request.address());
        company.setEmail(request.email());
        company.setActivityType(request.activityType());
        company.setCountry(request.country());
        company.setRegion(request.region());
        company.setStatus(request.status());

        companyRepository.save(company);
    }

    @Override
    public void createUserWithCompany(CreateUserRequest request) {
        if (accountRepository.existsByLoginIgnoreCase(request.login())) {
            throw new CustomException("Login already exists!");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Role role = roleRepository.findByNameIgnoreCase(request.role())
                .orElseThrow(() -> new NotFoundException("Not found role : " + request.role()));

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .role(role)
                .company(company)
                .build();
        userRepository.save(user);

        Account account = Account.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .active(request.active())
                .user(user)
                .build();
        accountRepository.save(account);
    }
}
