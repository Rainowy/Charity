package pl.coderslab.charity.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private DonationService donationService;
    private InstitutionService institutionService;
    private CategoryService categoryService;
    private UserService userService;

    public DonationController(DonationService donationService, InstitutionService institutionService, CategoryService categoryService, UserService userService) {
        this.donationService = donationService;
        this.institutionService = institutionService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @ModelAttribute("institutions")
    public List<Institution> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('USER')") //ROLE_USER też tu działą
    public ModelAndView donationForm(Principal principal) {
        ModelAndView model = new ModelAndView("form");
        Donation donation = new Donation();
        model.addObject("donation", donation);
        model.addObject("institutions");
        model.addObject("categories");
        Optional<User> user = userService.userByEmail(principal.getName());
        user.ifPresent(r ->model.addObject("userName",r.getFirstName()));
        return model;
    }

    @PostMapping("/form")
    public ModelAndView donationForm(Donation donation) {
        ModelAndView modelAndView = new ModelAndView("form-confirmation");
        donationService.saveDonation(donation);
//        modelAndView.setViewName("redirect:/donation/form");

        return modelAndView;
    }
}
