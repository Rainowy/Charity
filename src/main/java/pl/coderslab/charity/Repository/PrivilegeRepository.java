package pl.coderslab.charity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.charity.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);
}
