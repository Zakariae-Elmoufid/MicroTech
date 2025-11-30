package org.example.microTech.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.microTech.dto.ClientOrderStatsDTO;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
import org.example.microTech.services.ClientOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientOrderController {

    @Autowired
    private ClientOrderService clientOrderService;

    @GetMapping("/{clientId}/orders/stats")
    public ResponseEntity<ClientOrderStatsDTO> getClientStats(@PathVariable Long clientId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        return ResponseEntity.ok(clientOrderService.getClientStats(clientId));
    }
}
