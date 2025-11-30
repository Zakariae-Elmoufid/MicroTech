package org.example.microTech.services;


import lombok.AllArgsConstructor;
import org.example.microTech.entities.User;
import org.example.microTech.exceptions.BadRequestException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.repositories.UserRepository;
import org.example.microTech.utils.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements  UserService {

    private final UserRepository userRepository;


    public User getUser( String username,
                         String password) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        if (!PasswordUtil.verify(password,user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        return  user;
    }

}
