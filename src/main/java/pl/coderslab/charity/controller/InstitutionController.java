package pl.coderslab.charity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.charity.dto.InstitutionDto;
import pl.coderslab.charity.service.InstitutionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class InstitutionController {

    private InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ModelAttribute("institutions")
    public List<InstitutionDto> showAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/institutions")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView institutions() {
        ModelAndView model = new ModelAndView("admin/institutions","institutionDto", new InstitutionDto());
        model.addObject("institutions");
        return model;
    }

    @PostMapping("/addOrEditInstitution")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addOrEditInstitution(@Valid InstitutionDto institutionDto, BindingResult result) {
        ModelAndView model = new ModelAndView("redirect:/institutions");
        Optional<Long> id = Optional.ofNullable(institutionDto.getId());

        if (result.hasErrors()) {
            id.ifPresentOrElse(i -> model.addObject("unhide", "false"),
                    (() -> model.addObject("unhide", "true")));
            model.setViewName("admin/institutions");
            return model;
        }
        institutionService.saveInstitution(institutionDto);
        return model;
    }

    @GetMapping("/deleteInst/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteInstitution(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("redirect:/institutions");
        institutionService.deleteById(id);
        return model;
    }
}
