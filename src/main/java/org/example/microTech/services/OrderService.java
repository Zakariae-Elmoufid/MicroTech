package org.example.microTech.services;

import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;

public interface OrderService {

   public OrderResponseDTO createOrder(OrderRequestDTO dto);

}
