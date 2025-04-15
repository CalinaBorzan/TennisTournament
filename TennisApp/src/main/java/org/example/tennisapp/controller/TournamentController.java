package org.example.tennisapp.controller;

import org.example.tennisapp.entity.Tournament;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.UserRepository;
import org.example.tennisapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.example.tennisapp.service.TournamentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.findAllTournaments();
    }


    @GetMapping("/available")
    public ResponseEntity<List<Tournament>> getAvailableTournaments() {
        List<Tournament> availableTournaments = tournamentService.getAvailableTournaments();
        return ResponseEntity.ok(availableTournaments);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(
            @RequestParam Long tournamentId,
            @RequestParam Long userId
    ) {
        try { User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            tournamentService.registerUser(tournamentId, user.getId());
            return ResponseEntity.ok("Registered successfully");
        } catch (RuntimeException e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }
    }

    @GetMapping("/registered")
    public ResponseEntity<List<Tournament>> getRegisteredTournaments(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user.getRegisteredTournaments());
    }
}




