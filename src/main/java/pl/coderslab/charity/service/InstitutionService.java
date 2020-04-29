package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.entity.Institution;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<InstitutionPartialView> getAllInstitutionsProjection() {
        return institutionRepository.findAllByOrderByIdAsc();
    }


    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }

    public Optional<Institution> getById(Long id) {
        return institutionRepository.findById(id);
    }

    public Institution saveInstitution(Institution institution){
        Optional.ofNullable(institution.getId()).ifPresent(i ->
                institution.setDonations(getById(i).get().getDonations()));
        return institutionRepository.save(institution);
    }

    public void deleteById(Long id){
        institutionRepository.deleteById(id);
    }
}

