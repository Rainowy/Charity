package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.Repository.DonationRepository;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.service.DonationService;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
        ModelAndView model = new ModelAndView("user/tables");
        Donation donation = new Donation();
        model.addObject("donation",donation);
//        donationService.getAllDonationsProjection().stream()
//                .forEach(inst -> System.out.println(inst.getReceived() ));
        model.addObject("donations",donationService.getAllDonationsProjection());
        return model;
    }
//    @RequestParam boolean received
//@RequestParam String receiveddate

//    @RequestParam String receiveddate
    @PostMapping("/table")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView table(@Valid Donation donation, BindingResult result,
                              @RequestParam(required = false) String receiveddate){
        ModelAndView model = new ModelAndView("redirect:/user/panel");
        if (result.hasErrors()) {
            model.setViewName("redirect:/user/panel");
            return model;
        }

        LocalDateTime time = LocalDateTime.parse(receiveddate, DateTimeFormatter.ISO_DATE_TIME);


//        LocalDateTime dateTime = getLocalDateTimeFromString(time);
donation.setDatereceived(time);
//        donation.setDatereceived(receiveddate);
        System.out.println("ODEBRANO" + donation.isReceived());
        System.out.println(receiveddate);
//        System.out.println(date_received);
//        System.out.println(donation.getDatereceived());
        System.out.println(donation);

        return model;
    }


}
