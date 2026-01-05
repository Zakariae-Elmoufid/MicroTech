package org.example.microTech.repositories;

import org.example.microTech.entities.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderItemsRepository extends CrudRepository<OrderItem,Long> {
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.product.id = :productId AND oi.order.orderStatus = 'PENDING'")
    long countPendingOrdersByProductId(@Param("productId") Long productId);

}
