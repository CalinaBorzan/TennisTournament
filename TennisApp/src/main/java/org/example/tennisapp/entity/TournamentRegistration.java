package org.example.tennisapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_tournaments")           // reuse the existing join table
public class TournamentRegistration {

    @EmbeddedId
    private TournamentRegistrationId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    protected TournamentRegistration() {}   // JPA

    public TournamentRegistration(User player, Tournament tournament) {
        this.id = new TournamentRegistrationId(player, tournament);
    }


    // convenience accessors
    public User        getUser()       { return id.getUser();       }
    public Tournament  getTournament() { return id.getTournament(); }

    public void setUser(User u){ id.setUser(u); }
    public void setTournament(Tournament t){ id.setTournament(t); }

    public RegistrationStatus getStatus()          { return status; }
    public void setStatus(RegistrationStatus s)    { this.status = s; }


}
