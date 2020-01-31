package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.service.InstitutionService;

import java.util.List;

@Controller
public class HomeController {

    private InstitutionService institutionService;

    public HomeController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    //    @GetMapping("/")
//    @ResponseBody
//    public ModelAndView home() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//
//    }

    @GetMapping("/")
//    @ResponseBody
//    public List<InstitutionPartialView> home(Model model){
    public String home(Model model){

        List<InstitutionPartialView> allInstitutions = institutionService.getAllInstitutions();
        System.out.println(institutionService.getAllInstitutions());
        model.addAttribute("institutions",allInstitutions);


        for(InstitutionPartialView s : allInstitutions){
            System.out.println(s.getDescription() + " OPIS");

        }

//        return allInstitutions;
        return "index";
    }
}
