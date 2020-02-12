package pl.coderslab.charity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private InstitutionService institutionService;
    private DonationService donationService;

    public HomeController(InstitutionService institutionService, DonationService donationService) {
        this.institutionService = institutionService;
        this.donationService = donationService;
    }

    @ModelAttribute("institutions")
    List<InstitutionPartialView> showAllInstitutionsProjection(){
        return institutionService.getAllInstitutionsProjection();
    }

    @GetMapping()
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("institutions",showAllInstitutionsProjection());
        modelAndView.addObject("sumQuantities",donationService.sumQuantities());
        modelAndView.addObject("donationQuantities",donationService.donationQuantities());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/courses")
    public ModelAndView courses(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().stream()
                .forEach(System.out::println);
        System.out.println(auth.getName());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("courses");
        return modelAndView;
    }
}
