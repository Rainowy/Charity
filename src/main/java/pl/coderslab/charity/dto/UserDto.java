package pl.coderslab.charity.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.coderslab.charity.utils.DtoEntity;
import pl.coderslab.charity.validation.ValidPassword;
import pl.coderslab.charity.validation.ValidationStepOne;
import pl.coderslab.charity.validation.ValidationStepTwo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserDto implements DtoEntity {

    private Long id;
    @NotBlank(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{field.notEmpty}")
    private String firstName;
    @NotBlank(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{field.notEmpty}")
    private String lastName;
    @Email(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{email.regular}")
    @NotEmpty(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{email.notEmpty}")
    private String email;
    private String phone;
    @NotBlank(groups = ValidationStepOne.class, message = "{password.notEmpty}")
    @Length(min = 5, groups = ValidationStepOne.class, message = "{password.toShort}")
    @ValidPassword(groups = ValidationStepTwo.class)/** for profile edition purpose */
    private String password;
    private String avatar;
    private boolean enabled;
    private boolean isNotExpired;
}

