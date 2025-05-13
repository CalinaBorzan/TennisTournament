package org.example.tennisapp.service;

import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.UserRepository;
import org.example.tennisapp.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock  UserRepository  userRepo;
    @Mock  MatchRepository matchRepo;
    @Mock  PasswordEncoder encoder;

    @Test
    void registerUser_success() {
        when(userRepo.existsByUsername("alice")).thenReturn(false);
        when(userRepo.existsByEmail("alice@mail.com")).thenReturn(false);
        when(encoder.encode("pwd")).thenReturn("ENCODED");

        User saved = new User.Builder("alice","ENCODED","alice@mail.com", User.UserRole.player).build();
        when(userRepo.save(any())).thenReturn(saved);

        User result = userService.registerUser(
                "alice","pwd","alice@mail.com",
                User.UserRole.player,"Alice","Liddell"
        );

        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getPassword()).isEqualTo("ENCODED");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void registerUser_duplicateUsername_throws() {
        when(userRepo.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.registerUser("alice","pwd","x@mail.com",
                        User.UserRole.player,"A","B")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void registerUser_duplicateEmail_throws() {
        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(userRepo.existsByEmail("bob@mail.com")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.registerUser("bob","pwd","bob@mail.com",
                        User.UserRole.player,"B","Builder")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists.");
    }

    @Test
    void loginUser_wrongPassword_returnsNull() {
        User stored = new User.Builder(
                "bob",
                "HASH",
                "bob@mail.com",
                User.UserRole.player
        ).build();

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(stored));
        when(encoder.matches("bad", "HASH")).thenReturn(false);

        assertThat(userService.loginUser("bob","bad"))
                .isNull();
    }

    @Test
    void loginUser_success_returnsUser() {
        User stored = new User.Builder(
                "bob",
                "HASH",
                "bob@mail.com",
                User.UserRole.player
        ).build();

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(stored));
        when(encoder.matches("good", "HASH")).thenReturn(true);

        User result = userService.loginUser("bob","good");
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("bob");
    }

    @Test
    void loginUser_nonexistentUser_returnsNull() {
        when(userRepo.findByUsername("bob")).thenReturn(Optional.empty());

        assertThat(userService.loginUser("bob","anything"))
                .isNull();
    }

}
