package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.Repository.DonationRepository;
import pl.coderslab.charity.service.DonationService;

@Controller
@RequestMapping("/user")
public class UserController {

  DonationService donationService;

    public UserController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/panel")
    @PreAuthorize("hasRole('USER')") //ROLE_USER też tu działą
    public ModelAndView userPanel(){
//        ModelAndView model = new ModelAndView("user/user-panel");
        ModelAndView model = new ModelAndView("user/tables");
        donationService.getAllDonationsProjection().stream()
                .forEach(inst -> System.out.println(inst.getReceived() ));


        return model;
    }
}
