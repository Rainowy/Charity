package pl.coderslab.charity.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "authority_id")
    private Long id;
//    @Column(name = "name")
    private String name;
//    @ManyToMany(mappedBy = "authorities",
//            fetch = FetchType.EAGER)
//    private Set<Role> roles = new HashSet<>();
}
