package pl.coderslab.charity.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
@Entity
@Table(name="institution")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id")
    private Long id;
    @Length(min=3, message ="{field.nameToShort}")
    @NotBlank(message = "{field.notempty}")
    private String name;
    @Length(min=5 , message = "{field.descToShort}")
    @NotBlank(message = "{field.notempty}")
    private String description;
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();

    /**Synchro methods*/
    public void addDonation(Donation donation){
        donations.add(donation);
        donation.setInstitution(this);
    }
    public void removeDonation(Donation donation){
        donations.remove(donation);
        donation.setInstitution(null);
    }

//    @Override
//    public String toString() {
//        return "Institution{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", donations=" + donations +
//                '}';
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}
