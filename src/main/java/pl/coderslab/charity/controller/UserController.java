package pl.coderslab.charity.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.userStore.ActiveUserStore;
import pl.coderslab.charity.userStore.LoggedUser;
import pl.coderslab.charity.validation.ValidationStepTwo;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;
//import org.apache.commons.codec.digest.DigestUtils;

//import static org.apache.commons.codec.digest.DigestUtils.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;
    private DonationService donationService;
    private UserService userService;
    private String userAvatar;
    private String mailHash;

    public UserController(DonationService donationService, UserService userService) {
        this.donationService = donationService;
        this.userService = userService;
        this.userAvatar = "";
        this.mailHash = "";
    }

    @GetMapping("/donations")
    @PreAuthorize("hasRole('USER')") //ROLE_USER też tu działą
    public ModelAndView userPanel() {
        ModelAndView model = new ModelAndView("user/donations");
        Donation donation = new Donation();
        model.addObject("donation", donation);
//        model.addObject("donations",donationService.getAllDonationsProjection());
        model.addObject("donations", donationService.getAllDonations());

        return model;
    }

    @PostMapping("/table")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView table(@Valid Donation donation, BindingResult result) {
        ModelAndView model = new ModelAndView("redirect:/user/donations");
        if (result.hasErrors()) {
            model.setViewName("redirect:/user/donations");
            return model;
        }
        Optional<Donation> donationById = donationService.donationById(donation.getId());
        donationById.ifPresent(d -> d.setReceived(donation.isReceived()));
        donationById.ifPresent(d -> d.setDateReceived(donation.getDateReceived()));

        donationService.saveDonation(donationById.get());
        return model;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile() {
            /*
        Todo:

    */


        ModelAndView model = new ModelAndView("user/profile");
        userAvatar = userService.getCurrentUser().getAvatar();
        mailHash = userService.mailHash();
        model.addObject("user", userService.getCurrentUser());
        model.addObject("gravatar", mailHash);
        model.addObject("userAvatar", userAvatar);
        return model;
    }

    @PostMapping("/editprofile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile(@Validated(ValidationStepTwo.class) User user,
                                BindingResult result,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam(required = false) String password2) {
        ModelAndView model = new ModelAndView();
        /*
        Todo:

        3.Move saving image method to userservice

         */

        Optional.ofNullable(file)
                .stream()
                .filter(image -> !image.isEmpty() && !image.getOriginalFilename().equals(userAvatar)) //if true, the rest of the stream will run
                .peek(userService::saveAvatar)
                .map(MultipartFile::getOriginalFilename)
                .forEach(imgName -> userAvatar = imgName);

        userService.existenceValidator(user, result);

        if (Optional.ofNullable(password2).isPresent() && (!user.getPassword().equals(password2))) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("/user/profile");
            model.addObject("editEnabled", "true");
            model.addObject("userAvatar", userAvatar);
            model.addObject("gravatar",mailHash);
            return model;
        }
        user.setAvatar(userAvatar);
        userService.saveUser(user);

        model.setViewName("redirect:/user/profile");
        return model;
    }

    @GetMapping("/table_details/{id}")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView table_details(@PathVariable String id) {
        Optional<Donation> donationById = donationService.donationById(Long.parseLong(id));
        ModelAndView model = new ModelAndView("user/donations");

        model.addObject("details", "true");
        System.out.println(id);
        return model;
    }

    @GetMapping("/loggedUsers")
    @PreAuthorize("hasRole('USER')")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        model.addAttribute("usersId", activeUserStore.getUsersId());
        return "user/users";
    }
}
