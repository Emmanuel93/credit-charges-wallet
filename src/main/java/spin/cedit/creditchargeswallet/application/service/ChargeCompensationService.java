package spin.cedit.creditchargeswallet.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spin.cedit.creditchargeswallet.domain.model.Charge;
import spin.cedit.creditchargeswallet.domain.model.ChargeStatus;
import spin.cedit.creditchargeswallet.domain.repository.ChargeRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChargeCompensationService {
    private final ChargeRepository chargeRepository;

    public Charge compensateCharge(UUID transactionId) {
        Optional<Charge> optCharge = chargeRepository.findByTransactionId(transactionId);
        if (optCharge.isPresent()) {
            Charge charge = optCharge.get();
            charge.setStatus(ChargeStatus.CANCELADO);
            chargeRepository.save(charge);
        }
        return optCharge.get();
    }

    public Optional<Charge> setChargeGenerated(UUID transactionId) {
        Optional<Charge> optCharge = chargeRepository.findByTransactionId(transactionId);
        if (optCharge.isPresent()) {
            Charge charge = optCharge.get();
            charge.setStatus(ChargeStatus.GENERADO);
            chargeRepository.save(charge);
        }
        return optCharge;
    }
}
