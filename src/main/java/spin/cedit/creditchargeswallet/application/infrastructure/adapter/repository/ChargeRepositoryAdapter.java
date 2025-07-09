package spin.cedit.creditchargeswallet.application.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import spin.cedit.creditchargeswallet.domain.model.Charge;
import spin.cedit.creditchargeswallet.domain.repository.ChargeRepository;
import spin.cedit.creditchargeswallet.application.infrastructure.jpa.ChargeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ChargeRepositoryAdapter implements ChargeRepository {

    private final ChargeJpaRepository jpaRepository;

    public ChargeRepositoryAdapter(ChargeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Charge> findById(String chargeId) {
        return jpaRepository.findById(UUID.fromString(chargeId))
                .map(this::toDomain);
    }

    @Override
    public Optional<Charge> findByTransactionId(UUID transactionId) {
        return jpaRepository.findByTransactionId(transactionId)
                .map(this::toDomain);
    }

    @Override
    public List<Charge> findAll(int offset, int limit) {
        return jpaRepository.findAll()
                .stream()
                .skip(offset)
                .limit(limit)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Charge charge) {
        jpaRepository.save(fromDomain(charge));
    }

    private Charge toDomain(ChargeEntity entity) {
        Charge charge = new Charge();
        charge.setId(entity.getId());
        charge.setWalletId(entity.getWalletId());
        charge.setAmount(entity.getAmount());
        charge.setMonths(entity.getMonths());
        charge.setTransactionId(entity.getTransactionId());
        charge.setAmortizationType(entity.getAmortizationType());
        charge.setAnnualRate(entity.getAnnualRate());
        charge.setStatus(entity.getStatus());
        charge.setCreatedAt(entity.getCreatedAt());
        charge.setUpdatedAt(entity.getUpdatedAt());
        return charge;
    }

    private ChargeEntity fromDomain(Charge charge) {
        // Map domain to entity
        return ChargeEntity.builder()
                .id(charge.getId())
                .walletId(charge.getWalletId())
                .amount(charge.getAmount())
                .months(charge.getMonths())
                .transactionId(charge.getTransactionId())
                .amortizationType(charge.getAmortizationType())
                .annualRate(charge.getAnnualRate())
                .status(charge.getStatus())
                .createdAt(charge.getCreatedAt())
                .updatedAt(charge.getUpdatedAt())
                // detalles etc.
                .build();
    }
}
