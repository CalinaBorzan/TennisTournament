package org.example.tennisapp.service;

import org.example.tennisapp.entity.RegistrationStatus;
import org.example.tennisapp.entity.Tournament;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.repository.TournamentRegistrationRepository;
import org.example.tennisapp.repository.TournamentRepository;
import org.example.tennisapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @InjectMocks
    TournamentService service;

    @Mock
    TournamentRepository            tournamentRepo;
    @Mock
    UserRepository                  userRepo;
    @Mock
    TournamentRegistrationRepository regRepo;
    @Mock
    EmailService                    emailService;

    @Test
    void requestRegistration_createsPendingRecord() {
        var u = new User.Builder("cat","x","cat@mail.com", User.UserRole.player).build();
        u.setId(1L);
        var t = new Tournament(); t.setId(7L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(tournamentRepo.findById(7L)).thenReturn(Optional.of(t));
        when(regRepo.existsById(any())).thenReturn(false);

        service.requestRegistration(7L, 1L);

        ArgumentCaptor<org.example.tennisapp.entity.TournamentRegistration> cap =
                ArgumentCaptor.forClass(org.example.tennisapp.entity.TournamentRegistration.class);
        verify(regRepo).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(RegistrationStatus.PENDING);
    }

    @Test
    void requestRegistration_whenAlreadyExists_throws() {
        User u = new User.Builder("cat","x","cat@mail.com", User.UserRole.player).build();
        u.setId(1L);
        Tournament t = new Tournament(); t.setId(7L);

        when(tournamentRepo.findById(7L)).thenReturn(Optional.of(t));
        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(regRepo.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> service.requestRegistration(7L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
    }


    @Test
    void updateRegistrationStatus_sendsEmail() {
        when(regRepo.updateStatus(1L, 7L, RegistrationStatus.ACCEPTED))
                .thenReturn(1);

        var u = new User.Builder("cat","x","cat@mail.com", User.UserRole.player).build();
        u.setId(1L);
        var t = new Tournament(); t.setId(7L); t.setName("Spring Cup");

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(tournamentRepo.findById(7L)).thenReturn(Optional.of(t));

        service.updateRegistrationStatus(1L, 7L, RegistrationStatus.ACCEPTED);

        verify(emailService).sendSimpleEmail(
                eq("cat@mail.com"),
                contains("approved"),
                contains("Spring Cup")
        );
    }

    @Test
    void updateRegistrationStatus_whenNoRowsUpdated_throws() {
        when(regRepo.updateStatus(1L, 7L, RegistrationStatus.DENIED))
                .thenReturn(0);

        assertThatThrownBy(() ->
                service.updateRegistrationStatus(1L, 7L, RegistrationStatus.DENIED)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No such pending registration");
    }
}
