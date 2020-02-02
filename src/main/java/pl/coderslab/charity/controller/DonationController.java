package pl.coderslab.charity.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.List;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private InstitutionService institutionService;
    private CategoryService categoryService;

    public DonationController(InstitutionService institutionService, CategoryService categoryService) {
        this.institutionService = institutionService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("institutions")
    public List<Institution> getAllInstitutions(){
        return institutionService.getAllInstitutions();
    }

    @ModelAttribute("categoriess")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/form")
    public ModelAndView donationForm(){
        ModelAndView modelAndView = new ModelAndView();
        Donation donation = new Donation();
        modelAndView.addObject("donation",donation);
        modelAndView.addObject("institutions",getAllInstitutions());
        modelAndView.addObject("categoriess",getAllCategories());
        modelAndView.setViewName("form");
        return modelAndView;
    }

    @PostMapping("/form")
    public ModelAndView donationForm(Donation donation){
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(donation);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
