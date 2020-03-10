package pl.coderslab.charity.Repository;

import pl.coderslab.charity.entity.Institution;

import java.time.LocalDateTime;

public interface DonationPartialView {
    Long getId();
    Institution getInstitution();
    Boolean getReceived();
    LocalDateTime getCreated();
    LocalDateTime getDatereceived();
}
