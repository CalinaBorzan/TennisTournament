package org.example.tennisapp.dto;

public record PendingRegistrationDTO(
        Long userId,
        String username,
        Long tournamentId,
        String tournamentName,
        String userEmail
) {}
