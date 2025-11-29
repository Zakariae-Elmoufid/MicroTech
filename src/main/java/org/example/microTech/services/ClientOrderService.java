package org.example.microTech.services;

import org.example.microTech.dto.ClientOrderStatsDTO;

public interface ClientOrderService {
    public ClientOrderStatsDTO getClientStats(Long clientId);
}
