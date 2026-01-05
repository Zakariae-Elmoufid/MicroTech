package org.example.microTech.repositories;

import org.example.microTech.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndActive(Long id, boolean active);
}
