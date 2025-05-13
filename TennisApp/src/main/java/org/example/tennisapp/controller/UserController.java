package org.example.tennisapp.controller;

import org.example.tennisapp.dto.PlayerDTO;
import org.example.tennisapp.dto.UserDTO;
import org.example.tennisapp.dto.UserUpdateDTO;
import org.example.tennisapp.entity.User;
import org.example.tennisapp.service.PlayerQueryService;
import org.example.tennisapp.util.JwtUtil;
import org.example.tennisapp.util.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.tennisapp.service.UserService;
import org.example.tennisapp.service.EmailService;
import org.example.tennisapp.util.PlayerFilterRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PlayerQueryService playerQueryService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired private LoggedUser me;



    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole(),
                    user.getFirstName(),
                    user.getLastName()
            );
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> creds) {

        String username = creds.get("username");
        String password = creds.get("password");

        User u = userService.loginUser(username, password);
        if (u == null) return ResponseEntity.status(401).body("Invalid credentials");

        String token = jwtUtil.generateToken(u.getUsername(), u.getRole().name());

        Map<String,Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", new UserDTO(u));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('PLAYER','REFEREE','ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        boolean isAdmin     = me.current().getRole() == User.UserRole.admin;
        boolean isSelf      = me.current().getId().equals(id);

        if (!isAdmin && !isSelf)
            return ResponseEntity.status(403)
                    .body("You can delete only your own account");

        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','REFEREE')")
    public ResponseEntity<?> updateUserDetails(@PathVariable Long id,
                                               @RequestBody UserUpdateDTO dto) {

        boolean isAdmin = me.current().getRole() == User.UserRole.admin;
        boolean isSelf  = me.current().getId().equals(id);
        if (!isAdmin && !isSelf) {
            return ResponseEntity.status(403)
                    .body("You can update only your own profile");
        }

        User updated = userService.updateUserDetails(id, dto);
        return ResponseEntity.ok(updated);
    }


    @PostMapping("/admin/send-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    public ResponseEntity<?> sendEmailToUser(@RequestBody Map<String, String> body) {
        try {
            String to = body.get("to");
            String subject = body.get("subject");
            String content = body.get("content");

            emailService.sendSimpleEmail(to, subject, content);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/filter-players")
    @PreAuthorize("hasRole('ROLE_REFEREE')")
    public ResponseEntity<List<PlayerDTO>> filterPlayers(@RequestBody PlayerFilterRequest f){
        return ResponseEntity.ok(playerQueryService.filterPlayers(f));
    }

}


