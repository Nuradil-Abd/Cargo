package cargo.services.impl;

import cargo.dto.reponces.GetRoleResponse;

import cargo.dto.requests.RoleDeactivationRequest;
import cargo.dto.requests.RoleRequest;
import cargo.dto.requests.UpdateRoleRequest;
import cargo.entity.Role;
import cargo.entity.User;
import cargo.exeptions.BadRequestException;
import cargo.exeptions.NotFoundException;
import cargo.repositories.RoleRepository;
import cargo.repositories.UserRepository;
import cargo.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl  implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<GetRoleResponse> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(role -> GetRoleResponse.builder()
                        .roleName(role.getName())
                        .description(role.getDescription())
                        .usersCount(userRepository.countByRole(role))
                        .status(role.isStatus())
                        .build());
    }

    @Override
    public void createRole(RoleRequest request) {
        if (roleRepository.existsByNameIgnoreCase(request.name())) {
            throw new BadRequestException("Role with this name already exists");
        }

        Role role = Role.builder()
                .name(request.name().toUpperCase())
                .description(request.description())
                .status(true)
                .build();

        roleRepository.save(role);
    }


    @Override
    public void updateRole(String roleName, UpdateRoleRequest request) {
        Role role = roleRepository.findByNameIgnoreCase(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found with name: " + roleName));

        role.setName(request.name().toUpperCase());
        role.setDescription(request.description());
        role.setStatus(request.status());

        roleRepository.save(role);
    }

    @Override
    public void deactivateRoleWithAdminPassword(RoleDeactivationRequest request) {
        User admin = userRepository.findByRole_Name("ADMIN")
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        if (!passwordEncoder.matches(request.adminPassword(), admin.getPassword())) {
            throw new BadRequestException("Invalid admin password");
        }

        Role role = roleRepository.findByNameIgnoreCase(request.roleName())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        role.setStatus(false);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(String roleName, String adminPassword) {
        Role role = roleRepository.findByNameIgnoreCase(roleName)
                .orElseThrow(() -> new NotFoundException("Role with name: " + roleName + " not found"));

        User admin = userRepository.findByRole_Name("ADMIN")
                .orElseThrow(() -> new NotFoundException("Admin user not found"));

        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            throw new BadRequestException("Invalid admin password");
        }

        List<User> usersWithRole = userRepository.findAllByRole(role);
        for (User user : usersWithRole) {
            user.setRole(null);
        }
        userRepository.saveAll(usersWithRole);

        roleRepository.delete(role);
    }

}
