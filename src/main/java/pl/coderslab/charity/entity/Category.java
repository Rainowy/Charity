package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_id")
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Collection<Donation> donations;

        public void addDonation(Donation donation) {
        donations.add(donation);
//        donation.getCategories().add(this);
    }

    public void removeDonation(Donation donation) {
        donations.remove(donation);
//        donation.getCategories().remove(this);
    }
}

