package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.entity.VerificationToken;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.validation.ValidationStepOne;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private InstitutionService institutionService;
    private DonationService donationService;
    private UserService userService;
    private MessageSource messages;

    public HomeController(InstitutionService institutionService, DonationService donationService, UserService userService, @Qualifier("messageSource") MessageSource messages) {
        this.institutionService = institutionService;
        this.donationService = donationService;
        this.userService = userService;
        this.messages = messages;
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
        modelAndView.addObject("section", section);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("home/{section}")
    public ModelAndView links(@PathVariable String section, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/");
        redirectAttributes.addFlashAttribute("flashSection", section);
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
    public ModelAndView register(@Validated(ValidationStepOne.class) User user,
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

//    @Autowired
//    private IUserService service;


    @GetMapping("/regitrationConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {
            String messageValue = messages.getMessage("auth.message.invalidToken", null, locale);
            redirectAttributes.addFlashAttribute("message", messageValue);
            return "redirect:/registrationMessage";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            redirectAttributes.addFlashAttribute("message", messageValue);
            return "redirect:/registrationMessage";
        }
        String messageValue = messages.getMessage("auth.message.registered", null, locale);
        redirectAttributes.addFlashAttribute("message", messageValue);
        user.setEnabled(true);
        userService.update(user);
        return "redirect:/registrationMessage";
    }

    @GetMapping("/registrationMessage")
    public ModelAndView registrationMessage(@ModelAttribute("message") String message) {
        return new ModelAndView("register-confirmation", "message", message);
    }


    @GetMapping("/admin/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView admin(Principal principal) {
        ModelAndView model = new ModelAndView("admin/admin-panel");
        addUserNameToModel(principal, model);

        Map<String, Integer> collect = donationService.getAllDonations()
                .stream()
                .collect(Collectors.toMap(d -> d.getInstitution().getName(), Donation::getQuantity));

        List<String> instName = new ArrayList(collect.keySet());
        List<String> instQuantity = new ArrayList(collect.values());

        List<String> randomColor = instName.stream()
                .map(i -> String.format("#%06x", new Random().nextInt(0xffffff + 1)))
                .collect(Collectors.toList());

        model.addObject("colors", randomColor);
        model.addObject("institutionName", instName);
        model.addObject("allQuantities", instQuantity);
        return model;
    }

    private void addUserNameToModel(Principal principal, ModelAndView model) {
        Optional<User> user = userService.userByEmail(principal.getName());
        user.ifPresent(r -> model.addObject("userName", r.getFirstName()));
    }
}
