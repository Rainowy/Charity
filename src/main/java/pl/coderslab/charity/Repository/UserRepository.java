package pl.coderslab.charity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//   Optional <User> findByName(String name);
//   User findByName(String name);

    Optional <User> findByEmail(String email);
    Optional <User> findByFirstName(String name);
}
