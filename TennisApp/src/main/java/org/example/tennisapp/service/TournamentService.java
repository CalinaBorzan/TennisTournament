package org.example.tennisapp.service;

import jakarta.transaction.Transactional;
import org.example.tennisapp.entity.*;
import org.example.tennisapp.repository.TournamentRegistrationRepository;
import org.example.tennisapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.tennisapp.repository.TournamentRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired private TournamentRegistrationRepository regRepo;
    @Autowired private EmailService emailService;

    public List<Tournament> getAvailableTournaments() {
        LocalDate today = LocalDate.now();

        return tournamentRepository.findAll().stream()
                .filter(tournament -> !convertToLocalDate(tournament.getEndDate()).isBefore(today))
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }



    public List<Tournament> findAllTournaments() {
        return tournamentRepository.findAll();
    }




    @Transactional
    public void requestRegistration(Long tournamentId, Long userId) {

        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        User       u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TournamentRegistrationId id  = new TournamentRegistrationId(u, t);

        if (regRepo.existsById(id))
            throw new RuntimeException("A registration request already exists");

        TournamentRegistration reg = new TournamentRegistration(u, t);
        reg.setStatus(RegistrationStatus.PENDING);

        regRepo.save(reg);
    }

    @Transactional
    public void updateStatusInDb(Long userId,
                                 Long tournamentId,
                                 RegistrationStatus newStatus) {
        int rows = regRepo.updateStatus(userId, tournamentId, newStatus);
        if (rows == 0) {
            throw new RuntimeException("No such pending registration");
        }
    }

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(TournamentService.class);
    /** API method: update + then email (outside tx) **/
    public void updateRegistrationStatus(Long userId,
                                         Long tournamentId,
                                         RegistrationStatus newStatus) {

        // 1) update DB
        updateStatusInDb(userId, tournamentId, newStatus);

        // 2) load for email
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var tour = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        // 3) send notification
        String subj = "Tournament registration was " +
                (newStatus == RegistrationStatus.ACCEPTED ? "approved" : "denied");
        String body = "Hi " + user.getUsername() + ",\n" +
                "Your registration for \"" +
                tour.getName() + "\" has been " +
                newStatus.name().toLowerCase() + ".";
        emailService.sendSimpleEmail(user.getEmail(), subj, body);
        log.info("🟢 Sent registration‐status email to {}, subject={} ",user.getEmail(), subj);

    }

    public List<TournamentRegistration> pendingRegs() {
        return regRepo.findPendingWithUserAndTournament();
    }


}
