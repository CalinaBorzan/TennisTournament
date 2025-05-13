package org.example.tennisapp.repository;

//import org.example.tennisapp.dto.MatchDTO;
import org.example.tennisapp.entity.Match;
import org.example.tennisapp.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournament(Tournament tournament);

    List<Match> findByRefereeId(Long refereeId);

//    @Query("""
//    SELECT new org.example.tennisapp.dto.MatchDTO(
//      m.id, m.matchDate, m.score,
//      p1.id, p1.username,
//      p2.id, p2.username,
//      r.id,  r.username
//    )
//    FROM Match m
//    JOIN m.player1 p1
//    JOIN m.player2 p2
//    JOIN m.referee r
//    WHERE m.tournament.id = :tournamentId
//  """)
//    List<MatchDTO> findMatchDTOsByTournament(@Param("tournamentId") Long tournamentId);}
}