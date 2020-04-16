package pl.coderslab.charity.controller;

import be.ceau.chart.BarChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.BarData;
import be.ceau.chart.dataset.BarDataset;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.validation.ValidationStepOne;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private InstitutionService institutionService;
    private DonationService donationService;
    private UserService userService;

    public HomeController(InstitutionService institutionService, DonationService donationService, UserService userService) {
        this.institutionService = institutionService;
        this.donationService = donationService;
        this.userService = userService;
    }

    @ModelAttribute("institutions")
    List<InstitutionPartialView> showAllInstitutionsProjection() {
        return institutionService.getAllInstitutionsProjection();
    }

    @GetMapping()
    public ModelAndView home(@ModelAttribute("flashSection") String section) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("institutions", showAllInstitutionsProjection());
        modelAndView.addObject("sumQuantities", donationService.sumQuantities());
        modelAndView.addObject("donationQuantities", donationService.donationQuantities());
        modelAndView.addObject("section", section);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("home/{section}")
    public ModelAndView links(@PathVariable String section, RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView("redirect:/");
        redirectAttributes.addFlashAttribute("flashSection", section);
        return model;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("register");
        return model;
    }

    @PostMapping("/register")
    public ModelAndView register(@Validated(ValidationStepOne.class) User user,
                                 BindingResult result,
                                 @RequestParam String password2) {
        ModelAndView model = new ModelAndView();

        userService.existenceValidator(user, result);

        if (!user.getPassword().equals(password2)) {
            result.rejectValue("password", "messageCode", "Hasła muszą być takie same");
        }
        if (result.hasErrors()) {
            model.setViewName("/register");
            return model;
        }

        userService.saveUser(user);

        model.setViewName("register-confirmation");
        return model;
    }


    @GetMapping("/admin/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView admin(Principal principal) {
        ModelAndView model = new ModelAndView("admin/admin-panel");
        addUserNameToModel(principal, model);

        List<String> institutionName = showAllInstitutionsProjection()
                .stream()
                .map(InstitutionPartialView::getName)
                .collect(Collectors.toList());

        model.addObject("institutionName",institutionName);
        model.addObject("allQuantities",donationService.allQuantities());


//        BarDataset dataset = new BarDataset()
//                .setLabel("doughnut")
//                .setData(55, 30, 15)
//                .addBackgroundColors(Color.RED, Color.GREEN, Color.BLUE);
////                .setBorderWidth(2);
//
//        BarData data = new BarData()
//                .addLabels("Direct", "Referral", "Social")
//                .addDataset(dataset);



        BarDataset dataset = new BarDataset()
                .setLabel("sample chart")
                .setData(65, 59, 80, 81, 56, 55, 40)
                .addBackgroundColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.BLACK)
                .setBorderWidth(2);

        BarData data = new BarData()
                .addLabels("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                .addDataset(dataset);


        System.out.println(BarChart.data().toString());
//        return new BarChart(data).toJson();
//        model.addObject("chart", new BarChart(data).toJson());
//        model.addObject("chart", ale);

        return model;
    }

//    @ResponseBody
//    @GetMapping("/admin/panel")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String chartbar() {
//        BarDataset dataset = new BarDataset()
//                .setLabel("sample chart")
//                .setData(65, 59, 80, 81, 56, 55, 40)
//                .addBackgroundColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.BLACK)
//                .setBorderWidth(2);
//
//        BarData data = new BarData()
//                .addLabels("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
//                .addDataset(dataset);
//
//        return new BarChart(data).toJson();
//    }


    private void addUserNameToModel(Principal principal, ModelAndView model) {
        Optional<User> user = userService.userByEmail(principal.getName());
        user.ifPresent(r -> model.addObject("userName", r.getFirstName()));
    }
}
