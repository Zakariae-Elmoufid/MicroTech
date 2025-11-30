package org.example.microTech.repositories;

import org.example.microTech.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    public boolean existsByUserName(String username);
    public Optional<User> findByUserName(String username);

}
