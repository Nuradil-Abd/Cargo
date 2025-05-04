package cargo.repositories;

import cargo.entity.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByNameIgnoreCase(@NotBlank(message = "Role name cannot be blank") String name);
    Optional<Role> findByNameIgnoreCase(String roleName);
}
