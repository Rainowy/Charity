package pl.coderslab.charity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entity.Institution;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    List<InstitutionPartialView> findAllByOrderByIdAsc();

    List<Institution> findAll();

    Optional <Institution> findById(Long id);

}
