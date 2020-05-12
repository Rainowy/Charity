package pl.coderslab.charity.entity;//package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
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

    @OneToMany(mappedBy = "user")
    private List<Donation> donations = new ArrayList<>();

    /**
     * Synchro methods
     */
    public void addDonation(Donation donation) {
        donations.add(donation);
        donation.setUser(this);
    }

    public void removeDonation(Donation donation) {
        donations.remove(donation);
        donation.setInstitution(null);
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public boolean isTokenExpired() {
//        return tokenExpired;
//    }
//
//    public void setTokenExpired(boolean tokenExpired) {
//        this.tokenExpired = tokenExpired;
//    }
//
//    public Collection<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Collection<Role> roles) {
//        this.roles = roles;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public boolean isNotExpired() {
//        return isNotExpired;
//    }
//
//    public void setNotExpired(boolean notExpired) {
//        isNotExpired = notExpired;
//    }
//
//    public List<Donation> getDonations() {
//        return donations;
//    }
//
//    public void setDonations(List<Donation> donations) {
//        this.donations = donations;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", enabled=" + enabled +
//                ", tokenExpired=" + tokenExpired +
//                ", roles=" + roles +
//                '}';
//    }
}