package org.example.tennisapp.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany
    @JoinTable(
            name = "user_tournaments",          // name of the join table
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private List<Tournament> registeredTournaments = new ArrayList<>();

    protected User() {}

    public User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
    }

    public enum UserRole {
        admin,
        referee,
        player
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(User user) {
        return new Builder(user);
    }
    public static class Builder {
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String email;
        private UserRole role;

        public Builder() {}


        public Builder(User user) {
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.username = user.username;
            this.password = user.password;
            this.email = user.email;
            this.role = user.role;
        }

        public Builder(String username, String password, String email, UserRole role) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build() {
            validate();
            return new User(this);
        }

        void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (role == null) {
                throw new IllegalArgumentException("Role must be specified");
            }
        }


    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }

    public void setId(Long id) { this.id = id; }

    public List<Tournament> getRegisteredTournaments() {
        return registeredTournaments;
    }
    public void addTournament(Tournament tournament) {
        registeredTournaments.add(tournament);
    }

}