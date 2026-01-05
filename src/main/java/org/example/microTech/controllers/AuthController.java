package org.example.microTech.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.microTech.entities.User;
import org.example.microTech.repositories.UserRepository;
import org.example.microTech.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {


    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password,
                                        HttpSession session) {
        User user = userService.getUser(username, password);


        session.setAttribute("user", user);
        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
