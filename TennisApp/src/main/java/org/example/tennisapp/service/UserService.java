package org.example.tennisapp.service;

import jakarta.transaction.Transactional;
import org.example.tennisapp.dto.UserUpdateDTO;
import org.example.tennisapp.entity.Match;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.tennisapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    private MatchRepository matchRepository;


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User registerUser(String username, String rawPassword, String email,
                             User.UserRole role, String firstName, String lastName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User newUser = new User.Builder(username, passwordEncoder.encode(rawPassword), email, role)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        return userRepository.save(newUser);
    }

    public User loginUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(null);
    }


    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<Match> refereeMatches = matchRepository.findByRefereeId(id);
        for (Match match : refereeMatches) {
            match.setReferee(null);
        }

        List<Match> player1Matches = matchRepository.findAll().stream()
                .filter(m -> m.getPlayer1() != null && m.getPlayer1().getId().equals(id))
                .toList();
        for (Match match : player1Matches) {
            match.setPlayer1(null);
        }

        List<Match> player2Matches = matchRepository.findAll().stream()
                .filter(m -> m.getPlayer2() != null && m.getPlayer2().getId().equals(id))
                .toList();
        for (Match match : player2Matches) {
            match.setPlayer2(null);
        }

        matchRepository.saveAll(refereeMatches);
        matchRepository.saveAll(player1Matches);
        matchRepository.saveAll(player2Matches);

        userRepository.delete(user);
    }

    @Transactional
    public User updateUserDetails(Long id, UserUpdateDTO dto) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        /* ---------- username + email uniqueness checks ---------- */
        if (!u.getUsername().equals(dto.username())) {
            if (userRepository.existsByUsername(dto.username()))
                throw new IllegalArgumentException("Username already exists");
            u.setUsername(dto.username());
        }

        if (!u.getEmail().equals(dto.email())) {
            if (userRepository.existsByEmail(dto.email()))
                throw new IllegalArgumentException("Email already exists");
            u.setEmail(dto.email());
        }

        /* ---------- the rest of the scalar fields ---------- */
        u.setFirstName(dto.firstName());
        u.setLastName (dto.lastName ());
        if (dto.password() != null && !dto.password().isBlank())
            u.setPassword(passwordEncoder.encode(dto.password()));

        /* ---------- nothing else to do ---------- */
        return u;                 // TX commit will flush the dirty entity
    }




    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
