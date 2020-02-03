package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="institution")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id")
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();

    @Override
    public String toString() {
        return "Institution{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", donations=" + donations +
                '}';
    }
}
