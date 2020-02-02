package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.entity.Institution;

import java.util.List;

@Service
public class InstitutionService {

    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<InstitutionPartialView> getAllInstitutionsProjection() {
        return institutionRepository.findAllByOrderByIdAsc();
    }

    public List<Institution> getAllInstitutions(){
        return institutionRepository.findAll();
    }
}
