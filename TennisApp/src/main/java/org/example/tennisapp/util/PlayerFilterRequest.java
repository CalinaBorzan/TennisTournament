package org.example.tennisapp.util;

public record PlayerFilterRequest(
        String firstName,
        String lastName,
        String tournamentName

) { }
