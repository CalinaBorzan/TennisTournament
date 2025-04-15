package org.example.tennisapp.repository;

import org.example.tennisapp.entity.Match;
import org.example.tennisapp.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournament(Tournament tournament);

    List<Match> findByRefereeId(Long refereeId);
}