package org.example.microTech.repositories;

import org.example.microTech.entities.Payment;
import org.example.microTech.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findTopByOrderIdOrderByPaymentNumberDesc(Long orderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.paymentType = :type " +
            "AND p.paymentDate BETWEEN :startOfDay AND :endOfDay " +
            "AND p.order.client.id = :clientId")
    BigDecimal sumCashPaymentsForDay(@Param("type") PaymentType type,
                                     @Param("startOfDay") LocalDateTime startOfDay,
                                     @Param("endOfDay") LocalDateTime endOfDay,
                                     @Param("clientId") Long clientId);


    List<Payment> findByOrderId(Long orderId);

    @Query("SELECT p FROM Payment p ORDER BY p.paymentDate DESC")
    Page<Payment> findAllPayments(Pageable pageable);
}
