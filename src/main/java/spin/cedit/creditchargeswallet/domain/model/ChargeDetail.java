package spin.cedit.creditchargeswallet.domain.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChargeDetail {
    private UUID id;
    private int monthNumber;
    private LocalDateTime dueDate;
    private BigDecimal paymentAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
