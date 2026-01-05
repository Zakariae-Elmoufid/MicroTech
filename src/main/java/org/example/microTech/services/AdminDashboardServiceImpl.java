package org.example.microTech.services;

import lombok.AllArgsConstructor;
import org.example.microTech.dto.AdminDashboardStatsDTO;
import org.example.microTech.dto.OrderHistoryDTO;
import org.example.microTech.mappers.OrderMapper;
import org.example.microTech.repositories.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {


    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public AdminDashboardStatsDTO getDashboardStats() {

        long confirmed = orderRepository.countConfirmedOrders();
        long pending = orderRepository.countPendingOrders();
        long canceled = orderRepository.countCanceledOrders();
        long rejected = orderRepository.countRejectedOrders();

        BigDecimal totalConfirmedAmount = orderRepository.sumTotalConfirmedAmount();

        LocalDateTime firstOrder = orderRepository.firstOrderDate();
        LocalDateTime lastOrder = orderRepository.lastOrderDate();


        return new AdminDashboardStatsDTO(
                confirmed,
                pending,
                canceled,
                rejected,
                totalConfirmedAmount,
                firstOrder,
                lastOrder
        );
    }

}
