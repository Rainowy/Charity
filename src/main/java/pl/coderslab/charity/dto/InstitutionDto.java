package pl.coderslab.charity.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.utils.DtoEntity;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InstitutionDto implements DtoEntity {

    private Long id;
    @Length(min = 3, message = "{field.nameToShort}")
    @NotBlank(message = "{field.notempty}")
    private String name;
    @Length(min = 5, message = "{field.descToShort}")
    @NotBlank(message = "{field.notempty}")
    private String description;
    private List<Donation> donations = new ArrayList<>();
}

