package org.example.tennisapp.controller;

import org.example.tennisapp.dto.PendingRegistrationDTO;
import org.example.tennisapp.dto.RegistrationActionDTO;
import org.example.tennisapp.dto.TournamentDTO;
import org.example.tennisapp.entity.*;
import org.example.tennisapp.repository.TournamentRegistrationRepository;
import org.example.tennisapp.repository.UserRepository;
import org.example.tennisapp.util.JwtUtil;
import org.example.tennisapp.util.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.tennisapp.service.TournamentService;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRegistrationRepository regRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    LoggedUser me;


    @GetMapping
    public List<TournamentDTO> getAllTournaments() {
        return tournamentService.findAllTournaments().stream().map(TournamentDTO::new).toList();
    }


    @GetMapping("/available")
    @PreAuthorize("hasRole('PLAYER')")
    public List<TournamentDTO> available() {
        return tournamentService.getAvailableTournaments().stream().map(TournamentDTO::new).toList();
    }


    @GetMapping("/registered")
    @PreAuthorize("hasRole('PLAYER')")
    public List<TournamentDTO> getRegisteredTournaments() {
        return me.current().getRegisteredTournaments()
                .stream()
                .map(TournamentDTO::new)
                .toList();
    }


    @PostMapping("/request")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<?> requestRegistration(@RequestParam Long tournamentId) {

        tournamentService.requestRegistration(tournamentId, me.current().getId());
        return ResponseEntity.ok("Request submitted – waiting for admin approval");
    }


    @GetMapping("/registrations/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PendingRegistrationDTO> allPending() {
        return tournamentService.pendingRegs().stream()
                .map(r -> new PendingRegistrationDTO(
                        r.getUser().getId(),
                        r.getUser().getUsername(),
                        r.getTournament().getId(),
                        r.getTournament().getName(),
                        r.getUser().getEmail()
                ))
                .toList();
    }

    @PutMapping("/registrations/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approve(@RequestBody RegistrationActionDTO dto) {
        tournamentService.updateRegistrationStatus(
                dto.userId(), dto.tournamentId(), RegistrationStatus.ACCEPTED
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/registrations/deny")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deny(@RequestBody RegistrationActionDTO dto) {
        tournamentService.updateRegistrationStatus(
                dto.userId(), dto.tournamentId(), RegistrationStatus.DENIED
        );
        return ResponseEntity.ok().build();
    }

}




