package cargo.init;

import cargo.entity.Role;
import cargo.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = List.of("ADMIN", "USER", "CLIENT");


        for (String roleName : roles) {
            roleRepository.findByNameIgnoreCase(roleName)
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name(roleName)
                                    .description(roleName + " role")
                                    .status(true)
                                    .build()
                    ));
        }
    }

}
