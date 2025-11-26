package org.example.microTech.repositories;

import org.example.microTech.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order,Long> {
    List<Order> findByClientId(Long clientId);

}
