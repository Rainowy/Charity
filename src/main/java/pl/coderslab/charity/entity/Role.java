package pl.coderslab.charity.entity;//package pl.coderslab.charity.entity;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//@Data
//@Entity
//@Table(name = "role")
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    @Column(name = "role_id")
//    private Long id;
////    @Column(name = "name")
//    private String name;
//
//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {CascadeType.MERGE})
//    @JoinTable(
//            name = "role_authority",
//            joinColumns = {@JoinColumn(name = "role_id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_id")}
//    )
//    private Set<Authority> authorities = new HashSet<>();
//}
//
////    @ManyToMany
////    @JoinTable(name = "child_role", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
////    private Set<Role> roles;

import pl.coderslab.charity.entity.Privilege;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

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

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", privileges=" + privileges +
                '}';
    }
}