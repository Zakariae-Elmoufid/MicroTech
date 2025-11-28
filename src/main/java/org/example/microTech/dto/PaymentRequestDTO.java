    package org.example.microTech.dto;
    
    import jakarta.validation.constraints.DecimalMin;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Pattern;
    import org.example.microTech.enums.PaymentType;
    
    import java.math.BigDecimal;
    import java.time.LocalDate;
    
    public record PaymentRequestDTO (
            @NotNull(message = "Amount is required")
            @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
            BigDecimal amount,
    
            @NotNull(message = "Payment type is required")
            PaymentType type,
    
            String bank,
            LocalDate dueDate
    
            ){
    
        public void validatePaymentFields() {
            PaymentType paymentType = this.type;
    
            if(paymentType == PaymentType.CHECK) {
                if(bank == null || bank.isBlank()) {
                    throw new IllegalArgumentException("Bank is required for CHECK Payment");
                }
                if(dueDate == null) {
                    throw new IllegalArgumentException("Due date is required for CHECK");
                }
            }
    
            if(paymentType == PaymentType.BANK_TRANSFER) {
                if(bank == null || bank.isBlank()) {
                    throw new IllegalArgumentException("Bank is required for BANK_TRANSFER");
                }
            }
    
            if(paymentType == PaymentType.CASH) {
                if(bank != null || dueDate != null) {
                    throw new IllegalArgumentException("Bank and due date must be empty for CASH");
                }
            }
        }
    }
