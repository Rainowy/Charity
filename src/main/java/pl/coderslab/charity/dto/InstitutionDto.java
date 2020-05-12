package pl.coderslab.charity.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.coderslab.charity.entity.Donation;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode
public class InstitutionDto {

//    private Long id;
//    private String name;
//    private String description;
//    private String testowanie;

//    public InstitutionDto(Long id, String name, String description) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//    }


//    public InstitutionDto(Long id, String name, String description, String testowanie) {
//        this.id = id;
//        this.name = name;
////        this.description = description;
////        this.testowanie = testowanie;

    private Long id;
    @Length(min = 3, message = "{field.nameToShort}")
    @NotBlank(message = "{field.notempty}")
    private String name;
    @Length(min = 5, message = "{field.descToShort}")
    @NotBlank(message = "{field.notempty}")
    private String description;
    private List<Donation> donations = new ArrayList<>();
}

