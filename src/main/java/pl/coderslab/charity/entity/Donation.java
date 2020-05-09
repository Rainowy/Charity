package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//import java.time.LocalDateTime;

@Getter
@Setter
@Entity
//@NoArgsConstructor
@Table(name = "donation")
public class Donation {
    //    mappedBy = "donation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Long id;
    private int quantity;
    @OneToMany()
//            cascade = CascadeType.ALL)
//            orphanRemoval = true)
    @JoinColumn(name = "donation_id")
    private List<Category> categories = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;
    private String street;
    private String city;
    private String zipCode;
    private String phoneNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime pickUpTime;
    private String pickUpComment;
    private LocalDateTime created;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateReceived;
    private boolean received;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", categories=" + categories +
                ", institution=" + institution +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", pickUpDate=" + pickUpDate +
                ", pickUpTime=" + pickUpTime +
                ", pickUpComment='" + pickUpComment + '\'' +
                ", created=" + created +
                ", datereceived=" + dateReceived +
                ", received=" + received +
                '}';
    }
}



