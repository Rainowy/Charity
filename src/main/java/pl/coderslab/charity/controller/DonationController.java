package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private DonationService donationService;
//    private InstitutionService institutionService;
//    private CategoryService categoryService;
//    private UserService userService;

    public DonationController(DonationService donationService, InstitutionService institutionService, CategoryService categoryService, UserService userService) {
        this.donationService = donationService;
    }
//        this.institutionService = institutionService;
//        this.categoryService = categoryService;
//        this.userService = userService;
//    }
//
//    @ModelAttribute("institutions")
//    public List<InstitutionDto> getAllInstitutions() {
//        return institutionService.getAllInstitutions();
//    }
//    @ModelAttribute("categories")
//    public List<CategoryDto> getAllCategories() {
//        return categoryService.getAllCategories();
//    }


    @PostMapping("/form")
    public ModelAndView donationForm(DonationDto donationDto) {
        donationService.saveDonation(donationDto);
        return new ModelAndView("form-confirmation");
    }

}
