package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.charity.dto.InstitutionDto;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.entity.VerificationToken;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.validation.ValidationStepOne;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    List<InstitutionDto> showAllInstitutionsProjection() {
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

    @GetMapping("home/{section}")
    public ModelAndView links(@PathVariable String section, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/");
        redirectAttributes.addFlashAttribute("flashSection", section);
        return model;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();
        UserDto userDto = new UserDto();
        model.addObject("userDto", userDto);
        model.setViewName("register");
        return model;
    }

    @PostMapping("/register")
    public ModelAndView register(@Validated(ValidationStepOne.class) UserDto userDto,BindingResult result,   @RequestParam String password2, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView();

        userService.existenceValidator(userDto, result);

        if (!userDto.getPassword().equals(password2)) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("register");
            return model;
        }
        userService.saveUser(userDto);

        String messageValue = messages.getMessage("auth.message.confirmationEmail", null, Locale.getDefault());
        redirectAttributes.addFlashAttribute("message", messageValue);
        model.setViewName("redirect:/registrationMessage");
        return model;
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (WebRequest request, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

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
            String messageValue = messages.getMessage("auth.message.verification.expired", null, locale);
            redirectAttributes.addFlashAttribute("message", messageValue);
            return "redirect:/registrationMessage";
        }
        String messageValue = messages.getMessage("auth.message.registered", null, locale);
        redirectAttributes.addFlashAttribute("message", messageValue);
        userService.activateUser(user);
        return "redirect:/registrationMessage";
    }

    @GetMapping("/registrationMessage")
    public ModelAndView registrationMessage(@ModelAttribute("message") String message) {
        return new ModelAndView("register-confirmation", "message", message);
    }
}
