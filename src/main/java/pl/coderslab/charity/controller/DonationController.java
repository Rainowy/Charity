package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.service.DonationService;

import javax.validation.Valid;

@Controller
//@RequestMapping("/")
public class DonationController {

    private DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/form")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView donationForm(DonationDto donationDto) {
        donationService.saveDonation(donationDto);
        return new ModelAndView("redirect:/form-confirmation");
    }

    @GetMapping("/form-confirmation")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView formConfirmation(){
        return new ModelAndView("user/form-confirmation");
    }

    @GetMapping("/donations")
    @PreAuthorize("hasRole('USER')") //ROLE_USER też tu działą
    public ModelAndView donations() {
        ModelAndView model = new ModelAndView("user/donations", "donationDto", new DonationDto());
        model.addObject("donationsDto", donationService.getAllDonationsByUser());
        return model;
    }

    @PostMapping("/editDonations")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView editDonations(@Valid DonationDto donationDto, BindingResult result) {
        ModelAndView model = new ModelAndView("redirect:/donations");
        if (result.hasErrors()) {
            model.setViewName("redirect:/donations");
            return model;
        }
        donationService.editDonation(donationDto);
        return model;
    }
}
