package pl.coderslab.charity.entity;//package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
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
                    CascadeType.PERSIST,
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