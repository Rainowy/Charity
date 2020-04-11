package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
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
import pl.coderslab.charity.userStore.CurrentUser;
import pl.coderslab.charity.validation.ValidationStepTwo;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;
//import org.apache.commons.codec.digest.DigestUtils;

import static org.apache.commons.codec.digest.DigestUtils.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;


    private DonationService donationService;
    private UserService userService;
    private String userAvatar;

    public UserController(DonationService donationService, UserService userService) {
        this.donationService = donationService;
        this.userService = userService;
        this.userAvatar = "";
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
    public ModelAndView profile(@SessionAttribute("currentUser")CurrentUser currentUser) {
            /*
        Todo:

    */

        String mailToHash = currentUser.getUsername().trim().toLowerCase();
//        String hash = mailToHash;
        String password = mailToHash;
                System.out.println(mailToHash);
//        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        String md5Hex =  md5Hex(password).toLowerCase();

        System.out.println("ZAKODOWANE HASELKO " + md5Hex);
//        assertThat(md5Hex.equals(hash)).isTrue();


        ModelAndView model = new ModelAndView("user/profile");

        model.addObject("gravatar", md5Hex);

        model.addObject("user", userService.getCurrentUser());
        userAvatar = userService.getCurrentUser().getAvatar();
        model.addObject("userAvatar", userAvatar);
        return model;
    }

    //    private static String UPLOADED_FOLDER = "/home/tomek/Documents//";
    private static String UPLOADED_FOLDER = "/opt/files/";

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
