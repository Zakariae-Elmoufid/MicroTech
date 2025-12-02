package org.example.microTech.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.microTech.annotations.Secured;
import org.example.microTech.dto.AdminDashboardStatsDTO;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
import org.example.microTech.services.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @Secured(roles = UserRole.ADMIN)
    @GetMapping
    public AdminDashboardStatsDTO getStats(HttpSession session)
    {

        return dashboardService.getDashboardStats();
    }
}
