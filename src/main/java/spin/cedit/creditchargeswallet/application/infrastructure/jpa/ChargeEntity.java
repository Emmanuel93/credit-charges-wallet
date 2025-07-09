package spin.cedit.creditchargeswallet.application.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.*;
import spin.cedit.creditchargeswallet.domain.model.AmortizationType;
import spin.cedit.creditchargeswallet.domain.model.ChargeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "charges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "months", nullable = false)
    private int months;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "amortization_type")
    private AmortizationType amortizationType;

    @Column(name = "annual_rate")
    private BigDecimal annualRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChargeStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "charge", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChargeDetailEntity> details;
}
