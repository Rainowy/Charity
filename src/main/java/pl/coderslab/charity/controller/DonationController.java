package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.InstitutionDto;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.utils.DtoEntity;

import java.security.Principal;
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
    public List<InstitutionDto> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @ModelAttribute("categories")
    public List<DtoEntity> getAllCategories() {
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
        donationService.saveDonation(donation);
        return new ModelAndView("form-confirmation");
    }
}
