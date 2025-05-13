package org.example.tennisapp.dto;


public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;

    public UserDTO(Long id, String username, String email, String role, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(org.example.tennisapp.entity.User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name(), user.getFirstName(), user.getLastName());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;

    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;

    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;

    }


}
