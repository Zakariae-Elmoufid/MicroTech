package org.example.microTech.services;

import org.example.microTech.entities.User;

public interface UserService {
    public User getUser(String username, String password);

}
