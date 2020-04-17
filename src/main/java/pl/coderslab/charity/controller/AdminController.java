package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/institutions")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView institutions(){
        ModelAndView model = new ModelAndView("admin/institutions");
        return model;
    }
}
