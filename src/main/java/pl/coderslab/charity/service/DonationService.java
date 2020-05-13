package pl.coderslab.charity.service;


import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.DonationPartialView;
import pl.coderslab.charity.Repository.DonationRepository;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.utils.DtoUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    private DonationRepository donationRepository;
    private UserService userService;

    public DonationService(DonationRepository donationRepository, UserService userService) {
        this.donationRepository = donationRepository;
        this.userService = userService;
    }

    public Donation saveDonation(DonationDto donationDto) {

        Donation donation = (Donation) new DtoUtils().convertToEntity(new Donation(), donationDto);

        Institution institution = donation.getInstitution();
        List<Category> categories = donation.getCategories();
        User currentUser = userService.getCurrentUser();
        donation.setUser(currentUser);
        donation.setCategories(categories);

        categories.stream()  //synchro
                .forEach(category -> category.addDonation(donation));
        institution.addDonation(donation); //synchro
        currentUser.addDonation(donation); //synchro
        return donationRepository.save(donation);
    }

    public void editDonation(DonationDto donationDto) {
        Optional<Donation> donationById = getDonationById(donationDto.getId());
        donationById.ifPresent(d -> d.setReceived(donationDto.isReceived()));
        donationById.ifPresent(d -> d.setDateReceived(donationDto.getDateReceived()));
        donationById.ifPresent(donationRepository::save);
    }

    public Long sumQuantities() {
        return donationRepository.sumQuantities();
    }

    public List<String> allQuantities() {
        return donationRepository.allQuantities();
    }

    public Long donationQuantities() {
        return donationRepository.count();
    }

    public List<DonationPartialView> getAllDonationsProjection() {
        return donationRepository.findAllByOrderByIdAsc();
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public List<Donation> getAllDonationsByUser() {

        List<Donation> donations = donationRepository.findAllByUser(userService.getCurrentUser());
        PropertyMap<Donation, DonationDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getCategories()); //skip this properties
            }
        };
        return new DtoUtils().convertToDtoList(donations, new TypeToken<List<DonationDto>>() {
        }.getType(), propertyMap);

    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findDonationById(id);
    }
}