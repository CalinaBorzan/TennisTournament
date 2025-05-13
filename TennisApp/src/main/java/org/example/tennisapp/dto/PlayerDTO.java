package org.example.tennisapp.dto;

public record PlayerDTO(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email
) { }
