package spin.cedit.creditchargeswallet.domain.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Charge {
    private UUID id;
    private UUID walletId;
    private BigDecimal amount;
    private int months;
    private UUID transactionId;
    private AmortizationType amortizationType;
    private BigDecimal annualRate;
    private List<ChargeDetail> details;
    private ChargeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
