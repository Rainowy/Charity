package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.validation.ValidationStepTwo;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    DonationService donationService;
    UserService userService;

    public UserController(DonationService donationService, UserService userService) {
        this.donationService = donationService;
        this.userService = userService;
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
//        ModelAndView model = new ModelAndView("user/profile");
        ModelAndView model = new ModelAndView();
        String currentUserEmail = userService.getCurrentUser();
        Optional<User> currentUser = userService.userByEmail(currentUserEmail);
        model.addObject("user", currentUser.get());
        model.setViewName("user/profile");
        return model;
    }

    private static String UPLOADED_FOLDER = "/home/tomek/Documents//";

    @PostMapping("/editprofile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile(@Validated(ValidationStepTwo.class) User user,
                                BindingResult result,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam(required = false) String password2) {
        ModelAndView model = new ModelAndView();

        userService.existenceValidator(user, result);

        if(Optional.ofNullable(password2).isPresent() && (!user.getPassword().equals(password2))){
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }

        if (result.hasErrors()) {
            model.setViewName("/user/profile");
            model.addObject("editEnabled", "true");
            return model;
        }

        System.out.println(file.getOriginalFilename() + " TUTAJJJJ");
        System.out.println(file);
        if (file.isEmpty()) {
            System.out.println("JEST PUSTY");
        }
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }


        model.setViewName("redirect:/user/profile");
        System.out.println(user);

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

}
