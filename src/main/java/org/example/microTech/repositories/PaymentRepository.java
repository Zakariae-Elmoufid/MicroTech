package org.example.microTech.repositories;

import org.example.microTech.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
