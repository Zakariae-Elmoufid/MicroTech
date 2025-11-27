package org.example.microTech.repositories;

import org.example.microTech.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemsRepository extends CrudRepository<OrderItem,Long> {
}
