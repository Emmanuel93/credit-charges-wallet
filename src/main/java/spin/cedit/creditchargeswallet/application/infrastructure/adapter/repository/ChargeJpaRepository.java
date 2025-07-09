package spin.cedit.creditchargeswallet.application.infrastructure.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spin.cedit.creditchargeswallet.application.infrastructure.jpa.ChargeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChargeJpaRepository extends JpaRepository<ChargeEntity, UUID> {
    Optional<ChargeEntity> findByTransactionId(UUID transactionId);
}
