package pl.coderslab.charity.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.entity.Donation;

@Controller
@RequestMapping("/form")
public class DonationController {

    @GetMapping
    public ModelAndView donationForm(){
        ModelAndView modelAndView = new ModelAndView();
        Donation donation = new Donation();
        modelAndView.addObject("donation",donation);
//        modelAndView.addObject("street","ULICA");
        modelAndView.setViewName("form");
        return modelAndView;
    }


}
