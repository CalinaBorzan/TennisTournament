package org.example.tennisapp.service;

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
        return userRepository.findByUsername(username);
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
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
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
    public User updateUserDetails(Long id, User userUpdates) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    User.Builder builder = User.builder(existingUser);
                    if (userUpdates.getFirstName() != null) {
                        builder.firstName(userUpdates.getFirstName());
                    }
                    if (userUpdates.getLastName() != null) {
                        builder.lastName(userUpdates.getLastName());
                    }
                    if (userUpdates.getUsername() != null &&
                            !userUpdates.getUsername().equals(existingUser.getUsername())) {
                        if (userRepository.existsByUsername(userUpdates.getUsername())) {
                            throw new IllegalArgumentException("Username already exists");
                        }
                        builder.username(userUpdates.getUsername());
                    }
                    if (userUpdates.getEmail() != null &&
                            !userUpdates.getEmail().equals(existingUser.getEmail())) {
                        if (userRepository.existsByEmail(userUpdates.getEmail())) {
                            throw new IllegalArgumentException("Email already exists");
                        }
                        builder.email(userUpdates.getEmail());
                    }
                    if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
                        builder.password(passwordEncoder.encode(userUpdates.getPassword()));
                    } else {
                        builder.password(existingUser.getPassword());
                    }
                    if (userUpdates.getRole() != null && existingUser.getRole() != User.UserRole.admin) {
                        builder.role(userUpdates.getRole());
                    }

                    User updatedUser = builder.build();
                    updatedUser.setId(existingUser.getId());
                    return userRepository.save(updatedUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
