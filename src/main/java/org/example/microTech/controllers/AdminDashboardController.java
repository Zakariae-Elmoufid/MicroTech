package org.example.microTech.controllers;

import lombok.RequiredArgsConstructor;
import org.example.microTech.dto.AdminDashboardStatsDTO;
import org.example.microTech.services.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping
    public AdminDashboardStatsDTO getStats() {
        return dashboardService.getDashboardStats();
    }
}
