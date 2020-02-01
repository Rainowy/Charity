package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.Repository.InstitutionPartialView;

import java.util.List;

@Service
public class InstitutionService {

    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<InstitutionPartialView> getAllInstitutions() {
        return institutionRepository.findAllByOrderByIdAsc();
    }
}
