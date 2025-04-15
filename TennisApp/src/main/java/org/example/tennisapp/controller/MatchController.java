package org.example.tennisapp.controller;

import org.example.tennisapp.entity.Match;
import org.example.tennisapp.entity.Tournament;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.MatchRepository;
import org.example.tennisapp.repository.TournamentRepository;
import org.example.tennisapp.service.CsvMatchExporter;
import org.example.tennisapp.service.JsonMatchExporter;
import org.example.tennisapp.service.MatchExportStrategy;
import org.example.tennisapp.service.TxtMatchExporter;
import org.example.tennisapp.util.MatchExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<Match> getMatchesByTournament(@PathVariable Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        return matchRepository.findByTournament(tournament);
    }

    @GetMapping("/{matchId}")
    public Match getMatchById(@PathVariable Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }

    @GetMapping("/referee/{refereeId}")
    public List<Match> getMatchesByReferee(@PathVariable Long refereeId) {
        return matchRepository.findByRefereeId(refereeId);
    }

    @PutMapping("/{matchId}/update-score")
    public ResponseEntity<?> updateMatchScore(@PathVariable Long matchId, @RequestBody Map<String, String> request) {
        String score = request.get("score");

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setScore(score);
        matchRepository.save(match);

        return ResponseEntity.ok(match);
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportMatches(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) Long tournamentId,
            @RequestParam(required = false) Long refereeId,
            @RequestParam(required = false) Long playerId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        List<Match> matches = matchRepository.findAll();

        matches = matches.stream()
                .filter(m -> tournamentId == null || (m.getTournament() != null && m.getTournament().getId().equals(tournamentId)))
                .filter(m -> refereeId == null || (m.getReferee() != null && m.getReferee().getId().equals(refereeId)))
                .filter(m -> playerId == null ||
                        (m.getPlayer1() != null && m.getPlayer1().getId().equals(playerId)) ||
                        (m.getPlayer2() != null && m.getPlayer2().getId().equals(playerId)))
                .filter(m -> {
                    if (fromDate != null && toDate != null && m.getMatchDate() != null) {
                        LocalDate matchLocalDate = m.getMatchDate()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        LocalDate from = LocalDate.parse(fromDate);
                        LocalDate to = LocalDate.parse(toDate);

                        return (!matchLocalDate.isBefore(from) && !matchLocalDate.isAfter(to));
                    }
                    return true;
                })
                .toList();

        MatchExportStrategy strategy;
        switch (format.toLowerCase()) {
            case "json" -> strategy = new JsonMatchExporter();
            case "csv" -> strategy = new CsvMatchExporter();
            case "txt" -> strategy = new TxtMatchExporter();
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        }

        MatchExporter exporter = new MatchExporter(strategy);
        String exportedData = exporter.export(matches);

        return ResponseEntity.ok(exportedData);
    }

}
