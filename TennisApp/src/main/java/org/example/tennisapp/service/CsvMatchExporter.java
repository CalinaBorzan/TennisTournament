package org.example.tennisapp.service;

import org.example.tennisapp.entity.Match;

import java.util.List;

public class CsvMatchExporter implements MatchExportStrategy{

    @Override
    public String export(List<Match> matches){
        StringBuilder sb = new StringBuilder("Id,Player1,Player2,Referee,Score,Date\n");

        for(Match m : matches){
            sb.append(m.getId()).append(",")
                    .append(m.getPlayer1().getUsername()).append(",")
                    .append(m.getPlayer2().getUsername()).append(",")
                    .append(m.getReferee().getUsername()).append(",")
                    .append(m.getScore()).append(",")
                    .append(m.getMatchDate()).append("\n");

        }

        return sb.toString();

    }
}
