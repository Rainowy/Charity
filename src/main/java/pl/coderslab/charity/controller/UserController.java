package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.userStore.ActiveUserStore;
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
    public ModelAndView donations() {
        ModelAndView model = new ModelAndView("user/donations");
        model.addObject("donationDto", new DonationDto());
//        model.addObject("donations",donationService.getAllDonationsProjection());
        model.addObject("donationsDto", donationService.getAllDonationsByUser());

        return model;
    }

    @PostMapping("/donations")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView donations(@Valid DonationDto donationDto, BindingResult result) {
        ModelAndView model = new ModelAndView("redirect:/user/donations");
        if (result.hasErrors()) {
            model.setViewName("redirect:/user/donations");
            return model;
        }
        donationService.editDonation(donationDto);
        return model;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile() {

        ModelAndView model = new ModelAndView("user/profile", "user", userService.getCurrentUserDto());
        userAvatar = userService.getCurrentUserDto().getAvatar();
        mailHash = userService.mailHash();
//        model.addObject("user", userService.getCurrentUserDto());
        model.addObject("gravatar", mailHash);
        model.addObject("userAvatar", userAvatar);
        return model;
    }

    @PostMapping("/editProfile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile(@Validated(ValidationStepTwo.class) UserDto userDto,
                                BindingResult result,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam(required = false) String password2) {
        ModelAndView model = new ModelAndView();

        Optional.ofNullable(file)
                .stream()
                .filter(image -> !image.isEmpty() && !image.getOriginalFilename().equals(userAvatar)) //if true, the rest of the stream will run
                .peek(userService::saveAvatar)
                .map(MultipartFile::getOriginalFilename)
                .forEach(imgName -> userAvatar = imgName);

//        userService.existenceValidator(user, result);

        if (Optional.ofNullable(password2).isPresent() && (!userDto.getPassword().equals(password2))) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("/user/profile");
            model.addObject("editEnabled", "true");
            model.addObject("userAvatar", userAvatar);
            model.addObject("gravatar", mailHash);
            return model;
        }
        userDto.setAvatar(userAvatar);
        userService.updateUser(userDto);

        model.setViewName("redirect:/user/profile");
        return model;
    }

    @GetMapping("/table_details/{id}")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView table_details(@PathVariable String id) {
        Optional<Donation> donationById = donationService.getDonationById(Long.parseLong(id));
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
