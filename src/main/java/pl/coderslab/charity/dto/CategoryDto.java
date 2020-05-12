package pl.coderslab.charity.dto;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.charity.utils.DtoEntity;

@Getter
@Setter
public class CategoryDto implements DtoEntity {
    private Long id;
    private String name;
}
