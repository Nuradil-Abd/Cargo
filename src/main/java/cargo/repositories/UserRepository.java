package cargo.repositories;


import cargo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    @Query("select case when count(u) > 0 then true else false end from User u where u.email like :email")
    boolean existsByEmail(String email);
}
