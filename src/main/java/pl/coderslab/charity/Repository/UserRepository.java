package pl.coderslab.charity.Repository;

import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<T> findById(ID id);
    Optional <User> findByEmail(String email);
    Optional <User> findByFirstName(String name);
    User findUserById(Long id);


}