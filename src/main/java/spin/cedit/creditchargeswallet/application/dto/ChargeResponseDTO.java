package spin.cedit.creditchargeswallet.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ChargeResponseDTO implements Serializable {
    private UUID chargeId;
    private UUID transactionId;
    private String status;
    private String createdAt;  // ISO String fecha de creación
    private String updatedAt;  // ISO String fecha de actualización
    private List<Detail> amortizationTable = new ArrayList<>();

    @Data
    public static class Detail implements Serializable  {
        private String detailId;
        private int monthNumber;
        private String dueDate;           // ISO String
        private BigDecimal paymentAmount;
        private String createdAt;         // ISO String
        private String updatedAt;         // ISO String
    }
}
