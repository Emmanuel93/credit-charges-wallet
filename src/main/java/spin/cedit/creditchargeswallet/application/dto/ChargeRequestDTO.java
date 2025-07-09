package spin.cedit.creditchargeswallet.application.dto;

import lombok.Data;
import spin.cedit.creditchargeswallet.domain.model.AmortizationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChargeRequestDTO {
    private UUID walletId;
    private BigDecimal amount;
    private int months;
    private String firstDueDate;
    private BigDecimal annualRate;
    private AmortizationType amortizationType;
    private UUID transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}