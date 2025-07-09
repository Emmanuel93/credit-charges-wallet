package spin.cedit.creditchargeswallet.application.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "charge_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeDetailEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id", nullable = false)
    private ChargeEntity charge;

    @Column(name = "month_number")
    private int monthNumber;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "due_date")
    private LocalDateTime dueDate;
}
