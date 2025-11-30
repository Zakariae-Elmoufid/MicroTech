package org.example.microTech.services;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.microTech.entities.User;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@AllArgsConstructor
public class UserServiceImpl implements  UserService {

    private final UserRepository userRepository;


    public User getUser( String username,
                   String password) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getPassword().equals(password)) {
             new BadRequestException("Invalid credentials");
        }

     return  user;
    }

}
