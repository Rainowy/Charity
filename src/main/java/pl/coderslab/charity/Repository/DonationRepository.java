package pl.coderslab.charity.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entity.Donation;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query(value = "SELECT sum(quantity) FROM Donation ")
    Long sumQuantities();

    List<DonationPartialView> findAllByOrderByIdAsc();

    List<Donation> findAll();

    Optional<Donation> findDonationById(Long id);

    @Query(value = "SELECT quantity FROM Donation ORDER BY institution.id asc ")
    List<String> allQuantities();
}