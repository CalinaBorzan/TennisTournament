package org.example.tennisapp.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class TournamentRegistrationId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")           // << rename column
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)         // owning-side
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    protected TournamentRegistrationId() {}    // JPA

    public TournamentRegistrationId(User user, Tournament tournament) {
        this.user     = user;
        this.tournament  = tournament;
    }

    public User       getUser()       { return user; }
    public Tournament getTournament() { return tournament; }
    public void setUser(User u)       { this.user = u; }
    public void setTournament(Tournament t){ this.tournament = t; }

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof TournamentRegistrationId that)) return false;
        return Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(tournament.getId(), that.tournament.getId());
    }
    @Override public int hashCode(){
        return Objects.hash(user.getId(), tournament.getId());
    }
}
