package org.example.microTech.repositories;

import org.example.microTech.entities.Order;
import org.example.microTech.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository  extends JpaRepository<Order,Long> {
    List<Order> findByClientIdAndOrderStatus(Long clientId, OrderStatus orderStatus);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId AND o.orderStatus = 'PENDING'")
    int countClientOrdersPending(Long clientId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId AND o.orderStatus = 'CONFIRMED'")
    int countClientOrdersConfirmed(Long clientId);

    @Query("SELECT COALESCE(SUM(o.subTotal), 0) FROM Order o WHERE o.client.id = :clientId AND o.orderStatus = 'CONFIRMED'")
    BigDecimal sumClientConfirmedOrders(Long clientId);

    @Query("SELECT MIN(o.orderDate) FROM Order o WHERE o.client.id = :clientId")
    LocalDateTime firstClientOrderDate(Long clientId);

    @Query("SELECT MAX(o.orderDate) FROM Order o WHERE o.client.id = :clientId")
    LocalDateTime lastClientOrderDate(Long clientId);

    @Query("SELECT o FROM Order o WHERE o.client.id = :clientId ORDER BY o.orderDate DESC")
    List<Order> findAllByClient(Long clientId);


}
