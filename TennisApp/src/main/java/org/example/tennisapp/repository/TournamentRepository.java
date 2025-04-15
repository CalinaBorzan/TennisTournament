package org.example.tennisapp.repository;

import org.example.tennisapp.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
