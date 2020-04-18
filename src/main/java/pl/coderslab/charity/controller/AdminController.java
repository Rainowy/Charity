package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.InstitutionService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    InstitutionService institutionService;

    public AdminController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ModelAttribute("institutions")
    public List<Institution> showAllInstitutions(){
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/institutions")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView institutions(){
        ModelAndView model = new ModelAndView("admin/institutions");
        model.addObject("current",new Institution());
        model.addObject("institutions");


        return model;
    }
}
