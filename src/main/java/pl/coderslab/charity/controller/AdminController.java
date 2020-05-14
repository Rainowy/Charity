package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.service.AdminService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import java.util.List;

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

    @ModelAttribute("admins")
    public List<UserDto> allAdmins(){
        return userService.findAllAdmins();
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView admins() {
        ModelAndView model = new ModelAndView("admin/administrators", "userDto", new UserDto());
        model.addObject("admins");

        System.out.println(userService.findAllAdmins());

        return model;
    }

    @GetMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUser(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("redirect:/admin/admins");
        adminService.deleteById(id);
        return model;
    }
}
