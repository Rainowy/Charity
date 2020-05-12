package pl.coderslab.charity.service;

import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.dto.DtoUtils;
import pl.coderslab.charity.dto.InstitutionDto;
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

    public List<InstitutionDto> getAllInstitutions() {
        List<Institution> institutions = institutionRepository.findAll();

        /** Here we skip properties, if null pass as parameter, do not skip **/
        PropertyMap<Institution, InstitutionDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getDonations());  //skip this properties
            }
        };
        return new DtoUtils().convertToDtoList(institutions, new TypeToken<List<InstitutionDto>>() {
        }.getType(), propertyMap);
    }

    public Optional<Institution> getById(Long id) {
        return institutionRepository.findById(id);
    }

    public Institution saveInstitution(Institution institution) {
        Optional.ofNullable(institution.getId()).ifPresent(i ->
                institution.setDonations(getById(i).get().getDonations()));
        return institutionRepository.save(institution);
    }

    public void deleteById(Long id) {
        institutionRepository.deleteById(id);
    }
}

