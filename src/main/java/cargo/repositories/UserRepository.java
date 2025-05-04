package cargo.repositories;


import cargo.entity.Role;
import cargo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    @Query("select case when count(u) > 0 then true else false end from User u where u.email like :email")
    boolean existsByEmail(String email);

    long countByRole(Role role);

    Optional<User> findByRole_Name(String admin);

    List<User> findAllByRole(Role role);
}
