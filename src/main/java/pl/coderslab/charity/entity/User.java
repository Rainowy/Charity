package pl.coderslab.charity.entity;//package pl.coderslab.charity.entity;
//
//import org.hibernate.validator.constraints.Length;
//
//import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "email")
////    @Email(message = "{email.regularChild}")
//    private String email;
////    @Column(name = "password")
////    @Length(min = 5, message = "{password.length}")
////    @NotEmpty(message = "{password.notempty}")
//    private String password;
//    @Column(name = "username")
////    @NotEmpty(message = "{username.notempty}")
//    private String name;
//    @Column(name = "lastname")
////    @NotEmpty(message = "{lastname.notempty}")
//    private String lastname;
//
//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {CascadeType.MERGE})
//    @JoinTable(
//            name = "user_role",
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id")}
//    )
//    private Set<Role> roles = new HashSet<>();
//}

import pl.coderslab.charity.validation.ValidPassword;
import pl.coderslab.charity.validation.ValidationStepOne;
import pl.coderslab.charity.validation.ValidationStepTwo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{field.notempty}")
    private String firstName;
    @NotBlank(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{field.notempty}")
    private String lastName;
    @Email(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{email.regular}")
    @NotEmpty(groups = {ValidationStepOne.class, ValidationStepTwo.class}, message = "{email.notempty}")
    private String email;
    //    @NotBlank(message = "nie pusty")
    private String phone;
    @NotBlank(groups = ValidationStepOne.class, message = "{password.notempty}")
    @ValidPassword(groups = ValidationStepTwo.class)
//    , message = "{password.notEmptyOrLonger5}"
    private String password;
    private boolean enabled;
    private boolean isNotExpired;
    private boolean tokenExpired;
    private String avatar;

    public User() {
        super();
        this.enabled = false;
        this.isNotExpired = false;
    }

    @ManyToMany(
            cascade = { /** deletes user_roles but not Role and not Privilege */
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isNotExpired() {
        return isNotExpired;
    }

    public void setNotExpired(boolean notExpired) {
        isNotExpired = notExpired;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", tokenExpired=" + tokenExpired +
                ", roles=" + roles +
                '}';
    }
}