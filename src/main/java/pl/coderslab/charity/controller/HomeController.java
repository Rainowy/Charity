package pl.coderslab.charity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Controller
@RequestMapping("/")
public class HomeController {

    private InstitutionService institutionService;
    private DonationService donationService;
    private UserService userService;

    public HomeController(InstitutionService institutionService, DonationService donationService, UserService userService) {
        this.institutionService = institutionService;
        this.donationService = donationService;
        this.userService = userService;
    }

    @ModelAttribute("institutions")
    List<InstitutionPartialView> showAllInstitutionsProjection() {
        return institutionService.getAllInstitutionsProjection();
    }

    @GetMapping()
    public ModelAndView home(@ModelAttribute("flashSection") String section) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("institutions", showAllInstitutionsProjection());
        modelAndView.addObject("sumQuantities", donationService.sumQuantities());
        modelAndView.addObject("donationQuantities", donationService.donationQuantities());
        modelAndView.addObject("section",section);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @GetMapping("home/{section}")
    public ModelAndView links(@PathVariable String section, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView("redirect:/");
        redirectAttributes.addFlashAttribute("flashSection",section);
        return model;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("register");
        return model;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid User user,
                                 BindingResult result,
                                 @RequestParam String password2) {
        ModelAndView model = new ModelAndView();

        userService.existenceValidator(user, result);

        if (!user.getPassword().equals(password2)) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("/register");
            return model;
        }

        userService.saveUser(user);

        model.setViewName("register-confirmation");
        return model;
    }



    @GetMapping("/courses")
    public ModelAndView courses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().stream()
                .forEach(System.out::println);
        System.out.println(auth.getName());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("courses");
        return modelAndView;
    }
}
