package cargo.repositories;

import cargo.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @EntityGraph(attributePaths = {"user", "user.role"})
    Optional<Account> findByLogin( String login);

    Optional<Account> findByUserId(Long id);

    boolean existsByLoginIgnoreCase(String login);
}
