package org.example.tennisapp.dto;

public record UserUpdateDTO(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
) {}
