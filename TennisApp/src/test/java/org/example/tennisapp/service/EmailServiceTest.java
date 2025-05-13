package org.example.tennisapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;      // ← add this
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    EmailService emailService;

    @Test
    void sendSimpleEmail_invokesJavaMailSender() {
        emailService.sendSimpleEmail("a@b.com","sub","body");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}

