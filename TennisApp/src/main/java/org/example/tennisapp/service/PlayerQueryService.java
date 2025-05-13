// src/main/java/org/example/tennisapp/service/PlayerQueryService.java
package org.example.tennisapp.service;

import org.example.tennisapp.dto.PlayerDTO;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.UserRepository;
import org.example.tennisapp.util.PlayerFilterRequest;
import org.example.tennisapp.util.UserSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerQueryService {

    private final UserRepository repo;

    public PlayerQueryService(UserRepository repo) {
        this.repo = repo;
    }

    public List<PlayerDTO> filterPlayers(PlayerFilterRequest f) {
        Specification<User> spec = UserSpecs.isPlayer();

        if (f.firstName() != null && !f.firstName().isBlank())
            spec = spec.and(UserSpecs.firstNameLike(f.firstName()));

        if (f.lastName() != null && !f.lastName().isBlank())
            spec = spec.and(UserSpecs.lastNameLike(f.lastName()));



        if (f.tournamentName() != null && !f.tournamentName().isBlank())
            spec = spec.and(UserSpecs.acceptedInTournamentByName(f.tournamentName()));


        return repo.findAll(spec).stream()
                .map(u -> new PlayerDTO(
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getUsername(),
                        u.getEmail()))
                .toList();
    }
}
