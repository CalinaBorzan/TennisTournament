// src/main/java/org/example/tennisapp/repository/TournamentRegistrationRepository.java
package org.example.tennisapp.repository;

import org.example.tennisapp.entity.RegistrationStatus;
import org.example.tennisapp.entity.TournamentRegistration;
import org.example.tennisapp.entity.TournamentRegistrationId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TournamentRegistrationRepository
        extends JpaRepository<TournamentRegistration, TournamentRegistrationId> {

    // bulk-update the status for the given composite key
    @Modifying
    @Transactional
    @Query("""
    UPDATE TournamentRegistration r
       SET r.status = :status
     WHERE r.id.user.id = :userId
       AND r.id.tournament.id = :tournamentId
  """)
    int updateStatus(
            @Param("userId")       Long userId,
            @Param("tournamentId") Long tournamentId,
            @Param("status")       RegistrationStatus status
    );

    // fetch only PENDING + join fetch user+tournament
    @Query("""
    SELECT r FROM TournamentRegistration r
      JOIN FETCH r.id.user u
      JOIN FETCH r.id.tournament t
     WHERE r.status = 'PENDING'
  """)
    List<TournamentRegistration> findPendingWithUserAndTournament();
}
