package cargo.services.impl;

import cargo.entity.Role;
import cargo.exeptions.BadRequestException;
import cargo.repositories.RoleRepository;
import cargo.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl  implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role createRole(String name) {
        if (roleRepository.existsByName(name.toUpperCase())) {
            throw new BadRequestException("Role already exists");
        }
        Role role = Role.builder()
                .name(name.toUpperCase())
                .build();
        return roleRepository.save(role);
    }
}
