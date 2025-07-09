package spin.cedit.creditchargeswallet.domain.repository;

import spin.cedit.creditchargeswallet.domain.model.Charge;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChargeRepository {
    Optional<Charge> findById(String id);
    Optional<Charge> findByTransactionId(UUID transactionId);
    List<Charge> findAll(int offset, int limit);
    void save(Charge charge);
}
