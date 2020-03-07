package pl.coderslab.charity.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.DonationPartialView;
import pl.coderslab.charity.Repository.DonationRepository;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;

import java.util.List;

@Service
public class DonationService {

    private DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation saveDonation(Donation donation) {
        Institution institution = donation.getInstitution();
        institution.addDonation(donation);
        return donationRepository.save(donation);
    }
    public Long sumQuantities() {
        return donationRepository.sumQuantities();
    }

    public Long donationQuantities() {
        return donationRepository.count();
    }

//    public List<DonationPartialView> getAllDonationsProjection() {
//        return donationRepository.findAllByOrderByIdAsc();
//    }
    public List<DonationPartialView> getAllDonationsProjection() {
        return donationRepository.findAllByOrderByIdAsc();
    }
}
