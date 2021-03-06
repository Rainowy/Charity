package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.userStore.ActiveUserStore;
import pl.coderslab.charity.validation.ValidationStepTwo;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    ActiveUserStore activeUserStore;
    private UserService userService;
    private String userAvatar;
    private String mailHash;
    private String avatarUrl;

    public UserController(UserService userService, ActiveUserStore activeUserStore) {
        this.userService = userService;
        this.activeUserStore = activeUserStore;
//        this.userAvatar;
//        this.mailHash = "";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView profile() {

        ModelAndView model = new ModelAndView("user/profile", "userDto", userService.getCurrentUserDto());
        this.userAvatar = userService.getCurrentUserDto().getAvatar();
        this.avatarUrl = userService.avatarUrl(userAvatar);
        mailHash = userService.mailHash();
        model.addObject("gravatar", mailHash);
        model.addObject("userAvatar", avatarUrl);
        return model;
    }

    @PostMapping("/editProfile")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView editProfile(@Validated(ValidationStepTwo.class) UserDto userDto,
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

        userService.existenceValidator(userDto, result);
        this.avatarUrl = userService.avatarUrl(userAvatar);

        if (Optional.ofNullable(password2).isPresent() && (!userDto.getPassword().equals(password2))) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("/user/profile");
            model.addObject("editEnabled", "true");
            model.addObject("userAvatar", avatarUrl);
            model.addObject("gravatar", mailHash);
            return model;
        }
        userDto.setAvatar(userAvatar);
        userService.updateUser(userDto);

        model.setViewName("redirect:/user/profile");
        return model;
    }

    @GetMapping("/loggedUsers")
    @PreAuthorize("hasRole('USER')")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        model.addAttribute("usersId", activeUserStore.getUsersId());
        return "user/users";
    }

    @GetMapping("/delete")
    public String deleteUser() {
        userService.deleteByUser();
        return "redirect:/user/loggedUsers";
    }
}
