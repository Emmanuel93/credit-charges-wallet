package spin.cedit.creditchargeswallet.application.usecase;

import spin.cedit.creditchargeswallet.application.dto.ChargeRequestDTO;
import spin.cedit.creditchargeswallet.application.dto.ChargeResponseDTO;

public interface CreateChargeUseCase {
    ChargeResponseDTO createCharge(ChargeRequestDTO request);
}
