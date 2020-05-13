package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.CategoryDto;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.dto.InstitutionDto;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/logged")
public class LoggedInController {

    private UserService userService;
    private DonationService donationService;
    private CategoryService categoryService;
    private InstitutionService institutionService;

    public LoggedInController(DonationService donationService, UserService userService, InstitutionService institutionService, CategoryService categoryService) {
        this.userService = userService;
        this.donationService = donationService;
        this.categoryService = categoryService;
        this.institutionService = institutionService;
    }

    @ModelAttribute("institutions")
    public List<InstitutionDto> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("donations")
    public List<DonationDto> getAllDonations() {
        return donationService.getAllDonations();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView admin(Principal principal) {
        ModelAndView model = new ModelAndView("admin/admin-panel");

        addUserNameToModel(principal, model);

        /** If duplicates exist, group them in one and sum its quantity values */
        Map<String, Integer> collect = getAllDonations().stream().collect(
                Collectors.groupingBy(d -> d.getInstitution().getName(), Collectors.summingInt(DonationDto::getQuantity)));

        List<String> instName = new ArrayList(collect.keySet());
        List<String> instQuantity = new ArrayList(collect.values());
        List<String> randomColor = instName.stream()
                .map(i -> String.format("#%06x", new Random().nextInt(0xffffff + 1)))
                .collect(Collectors.toList());

        model.addObject("colors", randomColor);
        model.addObject("institutionName", instName);
        model.addObject("allQuantities", instQuantity);
        return model;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')") //ROLE_USER też tu działą
    public ModelAndView donationForm(Principal principal) {
        ModelAndView model = new ModelAndView("user-panel", "donationDto", new DonationDto());
//        model.addObject("donationDto", new DonationDto());
        model.addObject("institutions");
        model.addObject("categories");
        Optional<User> user = userService.userByEmail(principal.getName());
        user.ifPresent(r -> model.addObject("userName", r.getFirstName()));
        return model;
    }

    private void addUserNameToModel(Principal principal, ModelAndView model) {
        Optional<User> user = userService.userByEmail(principal.getName());
        user.ifPresent(r -> model.addObject("userName", r.getFirstName()));
    }

    @GetMapping("/logout_me")
    @PreAuthorize("hasRole('USER')")
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName() + " AUTENTYFIKACJA");
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "user/users";
    }
}

