package pl.coderslab.charity.service;

import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.utils.DtoUtils;
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

//    public List<InstitutionPartialView> getAllInstitutionsProjection() {
//        return institutionRepository.findAllByOrderByIdAsc();
//    }

    public List<InstitutionDto> getAllInstitutionsProjection() {

        List<Institution> institutions = institutionRepository.findAll();
        PropertyMap<Institution, InstitutionDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId()); //skip this properties
                skip(destination.getDonations());
            }
        };
        return new DtoUtils().convertToDtoList(institutions, new TypeToken<List<InstitutionDto>>() {
        }.getType(), propertyMap);
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
        List<InstitutionDto> institutionDtos = new DtoUtils().convertToDtoList(institutions, new TypeToken<List<InstitutionDto>>() {
        }.getType(), propertyMap);

//        institutionDtos.stream()
//                .map(s -> s.getDonations())
//                .forEach(s -> new DtoUtils().convertToDto(s, new DonationDto));

        return institutionDtos;
    }

    public Optional<Institution> getById(Long id) {
        return institutionRepository.findById(id);
    }

    public Institution saveInstitution(InstitutionDto institutionDto) {

        Institution institution = (Institution) new DtoUtils().convertToEntity(new Institution(), institutionDto);

        Optional.ofNullable(institution.getId()).ifPresent(i ->
                institution.setDonations(getById(i).get().getDonations()));
        return institutionRepository.save(institution);
    }

    public void deleteById(Long id) {
        institutionRepository.deleteById(id);
    }
}

