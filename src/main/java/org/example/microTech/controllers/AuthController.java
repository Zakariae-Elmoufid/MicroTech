package org.example.microTech.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.microTech.entities.User;
import org.example.microTech.repositories.UserRepository;
import org.example.microTech.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // remove session
        return ResponseEntity.ok("Logout successful");
    }
}
