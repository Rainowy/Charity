package pl.coderslab.charity.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class InstitutionDto {

    private Long id;
    private String name;
//    private String description;
//    private String testowanie;

//    public InstitutionDto(Long id, String name, String description) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//    }


    public InstitutionDto(Long id, String name, String description, String testowanie) {
        this.id = id;
        this.name = name;
//        this.description = description;
//        this.testowanie = testowanie;
    }
}
