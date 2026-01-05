package org.example.microTech.services;

import org.example.microTech.dto.AdminDashboardStatsDTO;
import org.example.microTech.dto.OrderHistoryDTO;
import org.springframework.data.domain.Page;

public interface AdminDashboardService {
    public AdminDashboardStatsDTO getDashboardStats();

}
