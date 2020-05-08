package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.AdminService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    InstitutionService institutionService;
    UserService userService;
    AdminService adminService;

    public AdminController(InstitutionService institutionService, UserService userService, AdminService adminService) {
        this.institutionService = institutionService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @ModelAttribute("institutions")
    public List<Institution> showAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/inst")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView institutions() {
        ModelAndView model = new ModelAndView("admin/institutions");
        Institution institution = new Institution();
        model.addObject("institution", institution);
        model.addObject("institutions");
        return model;
    }

    @PostMapping("/addOrEditInstitution")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addInstitutions(@Valid Institution institution, BindingResult result) {
        ModelAndView model = new ModelAndView("redirect:/admin/inst");
        Optional<Long> id = Optional.ofNullable(institution.getId());

        if (result.hasErrors()) {
            id.ifPresentOrElse(i -> model.addObject("unhide", "false"),
                    (() -> model.addObject("unhide", "true")));
            model.setViewName("admin/institutions");
            return model;
        }
        institutionService.saveInstitution(institution);
        return model;
    }

    @GetMapping("/deleteInst/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteInstitution(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("redirect:/admin/inst");
        System.out.println("ID do kasacji " + id);
        institutionService.deleteById(id);

        return model;
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView admins() {
        ModelAndView model = new ModelAndView("admin/administrators");

        System.out.println(userService.findAllAdmins());


        return model;
    }

    @GetMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUser(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("redirect:/admin/inst");
//        Optional<User> userToDelete = userService.userById(id);
        adminService.deleteById(id);
        return model;
    }
}
