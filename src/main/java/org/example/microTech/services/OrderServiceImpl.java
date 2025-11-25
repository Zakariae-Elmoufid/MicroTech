package org.example.microTech.services;


import lombok.AllArgsConstructor;
import org.example.microTech.repositories.ClientRepository;
import org.example.microTech.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
}
