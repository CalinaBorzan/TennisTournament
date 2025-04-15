package org.example.tennisapp.service;

import org.example.tennisapp.entity.Match;

import java.util.List;

public class TxtMatchExporter implements MatchExportStrategy {

    @Override
    public String export(List<Match> matches) {
        StringBuilder sb = new StringBuilder();

        for (Match m : matches) {
            sb.append("Match ID: ").append(m.getId()).append("\n")
                    .append("Tournament: ").append(m.getTournament() != null ? m.getTournament().getName() : "N/A").append("\n")
                    .append("Player 1: ").append(m.getPlayer1() != null ? m.getPlayer1().getUsername() : "N/A").append("\n")
                    .append("Player 2: ").append(m.getPlayer2() != null ? m.getPlayer2().getUsername() : "N/A").append("\n")
                    .append("Referee: ").append(m.getReferee() != null ? m.getReferee().getUsername() : "N/A").append("\n")
                    .append("Score: ").append(m.getScore() != null ? m.getScore() : "N/A").append("\n")
                    .append("Date: ").append(m.getMatchDate() != null ? m.getMatchDate() : "N/A").append("\n")
                    .append("--------------------------\n");
        }

        return sb.toString();
    }
}
