package org.example.tennisapp.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="match_table")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name="player1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name="player2_id")
    private User player2;

    @Column(nullable = false)
    private Date matchDate;

    @Column
    private String score;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private User referee;

    public Match() {}

    public Match(Tournament tournament, User p1, User p2, Date matchDate) {
        this.tournament = tournament;
        this.player1 = p1;
        this.player2 = p2;
        this.matchDate = matchDate;
    }

    public Long getId() { return id; }
    public Tournament getTournament() { return tournament; }
    public User getPlayer1() { return player1; }
    public User getPlayer2() { return player2; }
    public Date getMatchDate() { return matchDate; }
    public String getScore() { return score; }

    public User getReferee() {
        return referee;
    }

    public void setReferee(User referee) {
        this.referee = referee;
    }

    public void setId(Long id) { this.id = id; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }
    public void setPlayer1(User player1) { this.player1 = player1; }
    public void setPlayer2(User player2) { this.player2 = player2; }
    public void setMatchDate(Date matchDate) { this.matchDate = matchDate; }
    public void setScore(String score) { this.score = score; }

}
