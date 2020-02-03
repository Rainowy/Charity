package pl.coderslab.charity.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private DonationService donationService;
    private InstitutionService institutionService;
    private CategoryService categoryService;

    public DonationController(DonationService donationService, InstitutionService institutionService, CategoryService categoryService) {
        this.donationService = donationService;
        this.institutionService = institutionService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("institutions")
    public List<Institution> getAllInstitutions(){
        return institutionService.getAllInstitutions();
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/form")
    public ModelAndView donationForm(){
        ModelAndView modelAndView = new ModelAndView();
        Donation donation = new Donation();
        modelAndView.addObject("donation",donation);
        modelAndView.addObject("institutions");
        modelAndView.addObject("categories");
        modelAndView.setViewName("form");
        return modelAndView;
    }

    @PostMapping("/form")
    public ModelAndView donationForm(Donation donation){
        ModelAndView modelAndView = new ModelAndView();
        donationService.saveDonation(donation);
        modelAndView.setViewName("form-confirmation");

        return modelAndView;
    }
}
