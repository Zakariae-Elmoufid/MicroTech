package org.example.microTech.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,                // Payment ID
        int numeroPaiement,     // sequential number per order
        BigDecimal amount,      // montant payé
        String type,            // ESPÈCES / CHÈQUE / VIREMENT
        String reference,       // numéro reçu / chèque / virement
        String bank,            // bank name if applicable
        LocalDate dueDate,      // date d'échéance if deferred
        LocalDateTime paymentDate, // date de paiement effectif
        String status           // Encaissé / En attente / Rejeté
) {
}
