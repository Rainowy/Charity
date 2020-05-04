package pl.coderslab.charity.service;


import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.DonationPartialView;
import pl.coderslab.charity.Repository.DonationRepository;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;

import java.util.List;
import java.util.Optional;

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

    public List<String> allQuantities() { return donationRepository.allQuantities(); }

    public Long donationQuantities() {
        return donationRepository.count();
    }

    public List<DonationPartialView> getAllDonationsProjection() {
        return donationRepository.findAllByOrderByIdAsc();
    }
    public List<Donation> getAllDonations(){return donationRepository.findAll();}
    public Optional<Donation> donationById(Long id) {
        return donationRepository.findDonationById(id);
    }
}