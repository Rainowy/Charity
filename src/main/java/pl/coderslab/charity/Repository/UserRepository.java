package pl.coderslab.charity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional <User> findByEmail(String email);
    Optional <User> findByFirstName(String name);
    Optional <User> findUserById(Long id);

    List<User> findAllByRoles(Role role);

    }

