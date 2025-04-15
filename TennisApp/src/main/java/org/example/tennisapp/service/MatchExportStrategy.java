package org.example.tennisapp.service;

import org.example.tennisapp.entity.Match;

import java.util.List;

public interface MatchExportStrategy {
    String export(List<Match> matches);
}
