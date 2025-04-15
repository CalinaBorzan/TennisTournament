package org.example.tennisapp.util;

import org.example.tennisapp.entity.Match;
import org.example.tennisapp.service.MatchExportStrategy;

import java.util.List;

public class MatchExporter {
    private MatchExportStrategy strategy;
    public MatchExporter(MatchExportStrategy strategy) {
        this.strategy = strategy;
    }
    public String export(List<Match> match) {
        return strategy.export(match);
    }
}
