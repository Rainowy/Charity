package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.InstitutionService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    InstitutionService institutionService;

    public AdminController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ModelAttribute("institutions")
    public List<Institution> showAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/inst")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView institutions() {
//        ModelAndView model = new ModelAndView("admin/institutions");
        ModelAndView model = new ModelAndView("admin/testowe");
        Institution institution = new Institution();
        model.addObject("institution", institution);
//        model.addObject("currentInstitution", new Institution());
        model.addObject("institutions");
        return model;
    }

    @PostMapping("/inst")
    @PreAuthorize("hasRole('ADMIN')")
//    public ModelAndView editInstitutions(@Validated(ValidationStepOne.class) Institution nowy, BindingResult result) {
    public ModelAndView editInstitutions(@Valid Institution institution, BindingResult result) {
        ModelAndView model = new ModelAndView();

//        result.rejectValue("name", "messageCode", "Hasła muszą być takie same");

        System.out.println(institution);

        System.out.println(institution.getName());
        System.out.println(institution.getDescription());


        if (result.hasErrors()) {

            System.out.println("jest błąd !!!");
//                    result.rejectValue("name", "messageCode", "Hasła muszą być takie same");
//            result.rejectValue("name", "messageCode", "Hasła muszą być takie same");
//            result.rejectValue("description", "messageCode", "Hasła muszą być takie same");
//            ModelAndView model = new ModelAndView("admin/institutions");
//            model.addObject("newInstitution", new Institution());
//            Institution nowyk = new Institution();
//            model.addObject("nowy", nowy);
            model.setViewName("admin/testowe");
            return model;
        }
//        return model;

        return new ModelAndView("admin/institutions");
    }
}
