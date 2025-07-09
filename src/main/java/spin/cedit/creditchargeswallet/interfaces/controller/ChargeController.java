package spin.cedit.creditchargeswallet.interfaces.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spin.cedit.creditchargeswallet.application.dto.ChargeRequestDTO;
import spin.cedit.creditchargeswallet.application.dto.ChargeResponseDTO;
import spin.cedit.creditchargeswallet.application.service.ChargeCompensationService;
import spin.cedit.creditchargeswallet.application.usecase.CreateChargeUseCase;
import spin.cedit.creditchargeswallet.domain.model.Charge;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("v1/api/charges")
public class ChargeController {
    private final CreateChargeUseCase useCase;
    private final ChargeCompensationService compensationService;

    public ChargeController(CreateChargeUseCase useCase, ChargeCompensationService compensationService) {
        this.useCase = useCase;
        this.compensationService = compensationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChargeResponseDTO create(@RequestBody ChargeRequestDTO req) {
        log.info("[CREATE] transactionId={} walletId={}", req.getTransactionId(), req.getWalletId());
        return useCase.createCharge(req);
    }

    @PostMapping("/compensate/{transactionId}")
    public ChargeResponseDTO compensate(@PathVariable String transactionId) {
        log.info("[COMPENSATE] transactionId={}", transactionId);
        UUID tid = UUID.fromString(transactionId);
        Charge charge = compensationService.compensateCharge(tid);
        return toResponseDTO(charge);
    }

    private ChargeResponseDTO toResponseDTO(Charge charge) {
        ChargeResponseDTO resp = new ChargeResponseDTO();
        resp.setChargeId(charge.getId());
        resp.setTransactionId(charge.getTransactionId());
        resp.setStatus(charge.getStatus().name());
        resp.setCreatedAt(charge.getCreatedAt() != null ? charge.getCreatedAt().toString() : null);
        resp.setUpdatedAt(charge.getUpdatedAt() != null ? charge.getUpdatedAt().toString() : null);

        charge.getDetails().forEach(detail -> {
            ChargeResponseDTO.Detail det = new ChargeResponseDTO.Detail();
            det.setDetailId(detail.getId().toString());
            det.setMonthNumber(detail.getMonthNumber());
            det.setDueDate(detail.getDueDate() != null ? detail.getDueDate().toString() : null);
            det.setPaymentAmount(detail.getPaymentAmount());
            det.setCreatedAt(detail.getCreatedAt() != null ? detail.getCreatedAt().toString() : null);
            det.setUpdatedAt(detail.getUpdatedAt() != null ? detail.getUpdatedAt().toString() : null);
            resp.getAmortizationTable().add(det);
        });

        return resp;
    }
}
