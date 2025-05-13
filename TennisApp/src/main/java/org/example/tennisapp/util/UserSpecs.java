package org.example.tennisapp.util;
import jakarta.persistence.criteria.Join;
import org.example.tennisapp.entity.*;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    public static Specification<User> isPlayer() {
        return (root, q, cb) ->
                cb.equal(root.get("role"), User.UserRole.player);
    }

    public static Specification<User> firstNameLike(String name) {
        return (root, q, cb) ->
                cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> lastNameLike(String name) {
        return (root, q, cb) ->
                cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%");
    }



    public static Specification<User> acceptedInTournamentByName(String tournamentName) {
        return (root, q, cb) -> {
            Join<User, TournamentRegistration> reg = root.join("registrations");
            Join<TournamentRegistration, Tournament> tour = reg.join("id").join("tournament");

            return cb.and(
                    cb.equal(reg.get("status"), RegistrationStatus.ACCEPTED),
                    cb.like(cb.lower(tour.get("name")), "%" + tournamentName.toLowerCase() + "%")
            );
        };
    }


}
