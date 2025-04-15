package org.example.tennisapp.service;

import jakarta.transaction.Transactional;
import org.example.tennisapp.entity.Tournament;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.tennisapp.repository.TournamentRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Tournament> getAvailableTournaments() {
        LocalDate today = LocalDate.now();

        return tournamentRepository.findAll().stream()
                .filter(tournament -> !convertToLocalDate(tournament.getEndDate()).isBefore(today))
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Transactional
    public void registerUser(Long tournamentId, Long userId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRegisteredTournaments().contains(tournament)) {
            throw new RuntimeException("User already registered for this tournament");
        }

        user.addTournament(tournament);
        tournament.addUser(user);

        userRepository.save(user);
        tournamentRepository.save(tournament);
    }

    public List<Tournament> findAllTournaments() {
        return tournamentRepository.findAll();
    }
    }
