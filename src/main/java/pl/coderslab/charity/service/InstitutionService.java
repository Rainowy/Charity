package pl.coderslab.charity.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.dto.InstitutionDto;
import pl.coderslab.charity.entity.Institution;

import java.lang.reflect.Type;
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
        Type listType = new TypeToken<List<InstitutionDto>>() {
        }.getType();

        PropertyMap<Institution, InstitutionDto> clientPropertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getDonations());  //skip this properties
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(clientPropertyMap);
        List<InstitutionDto> lista = modelMapper.map(institutions, listType);
        return lista;
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

