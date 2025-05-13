package org.example.tennisapp.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "id.tournament",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TournamentRegistration> registrations = new ArrayList<>();

    public void addRegistration(TournamentRegistration reg) {
        registrations.add(reg);
        reg.setTournament(this);
    }

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status; // Define this enum with PENDING, ACCEPTED, REJECTED

    public Tournament(Long id, String name, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Tournament() {}

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
