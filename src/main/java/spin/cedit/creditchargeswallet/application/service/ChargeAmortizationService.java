package spin.cedit.creditchargeswallet.application.service;

import org.springframework.transaction.annotation.Transactional;
import spin.cedit.creditchargeswallet.application.dto.ChargeRequestDTO;
import spin.cedit.creditchargeswallet.application.dto.ChargeResponseDTO;
import spin.cedit.creditchargeswallet.application.usecase.CreateChargeUseCase;
import spin.cedit.creditchargeswallet.domain.model.AmortizationType;
import spin.cedit.creditchargeswallet.domain.model.Charge;
import spin.cedit.creditchargeswallet.domain.model.ChargeDetail;
import spin.cedit.creditchargeswallet.domain.model.ChargeStatus;
import spin.cedit.creditchargeswallet.domain.repository.ChargeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChargeAmortizationService implements CreateChargeUseCase {

    private final ChargeRepository chargeRepository;

    public ChargeAmortizationService(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    @Override
    @Transactional
    public ChargeResponseDTO createCharge(ChargeRequestDTO req) {
        LocalDateTime now = LocalDateTime.now();
        Charge charge = new Charge();
        charge.setId(UUID.randomUUID());
        charge.setTransactionId(req.getTransactionId());
        charge.setWalletId(req.getWalletId());
        charge.setAmount(req.getAmount());
        charge.setMonths(req.getMonths());
        charge.setCreatedAt(now);
        charge.setUpdatedAt(now);
        charge.setStatus(ChargeStatus.GENERADO);
        charge.setAmortizationType(req.getAmortizationType());
        charge.setAnnualRate(req.getAnnualRate());

        List<ChargeDetail> details = generateAmortizationTable(
                req.getAmount(), req.getMonths(), LocalDateTime.parse(req.getFirstDueDate()),
                req.getAnnualRate(), req.getAmortizationType(), now
        );

        charge.setDetails(details);
        chargeRepository.save(charge);

        ChargeResponseDTO resp = new ChargeResponseDTO();
        resp.setChargeId(charge.getId());
        resp.setTransactionId(charge.getTransactionId());
        resp.setStatus(charge.getStatus().toString());
        resp.setCreatedAt(charge.getCreatedAt().toString());
        resp.setUpdatedAt(charge.getUpdatedAt().toString());

        List<ChargeResponseDTO.Detail> respDetails = new ArrayList<>();
        for (ChargeDetail d : details) {
            ChargeResponseDTO.Detail det = new ChargeResponseDTO.Detail();
            det.setDetailId(d.getId().toString());
            det.setMonthNumber(d.getMonthNumber());
            det.setDueDate(d.getDueDate().toString());
            det.setPaymentAmount(d.getPaymentAmount());
            det.setCreatedAt(d.getCreatedAt() != null ? d.getCreatedAt().toString() : null);
            det.setUpdatedAt(d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : null);
            respDetails.add(det);
        }
        resp.setAmortizationTable(respDetails);

        return resp;
    }


    private List<ChargeDetail> generateAmortizationTable(
            BigDecimal amount,
            int months,
            LocalDateTime firstDueDate,
            BigDecimal annualRate,
            AmortizationType type,
            LocalDateTime now) {

        List<ChargeDetail> details = new ArrayList<>();
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        LocalDateTime dueDate = firstDueDate;

        if (AmortizationType.FRANCESA.equals(type)) {
            BigDecimal one = BigDecimal.ONE;
            BigDecimal ratePlusOnePowN = one.add(monthlyRate).pow(months);
            BigDecimal numerator = amount.multiply(monthlyRate).multiply(ratePlusOnePowN);
            BigDecimal denominator = ratePlusOnePowN.subtract(one);
            BigDecimal cuota = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

            for (int i = 1; i <= months; i++) {
                ChargeDetail d = new ChargeDetail();
                d.setId(UUID.randomUUID());
                d.setMonthNumber(i);
                d.setPaymentAmount(cuota);
                d.setDueDate(dueDate.plusMonths(i - 1));
                d.setCreatedAt(now);
                d.setUpdatedAt(now);
                details.add(d);
            }
        } else if (AmortizationType.AMERICANA.equals(type)) {
            BigDecimal interestPayment = amount.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);

            for (int i = 1; i <= months; i++) {
                ChargeDetail d = new ChargeDetail();
                d.setId(UUID.randomUUID());
                d.setMonthNumber(i);
                d.setDueDate(dueDate.plusMonths(i - 1));
                d.setCreatedAt(now);
                d.setUpdatedAt(now);

                if (i < months) {
                    d.setPaymentAmount(interestPayment);
                } else {
                    BigDecimal lastPayment = interestPayment.add(amount).setScale(2, RoundingMode.HALF_UP);
                    d.setPaymentAmount(lastPayment);
                }
                details.add(d);
            }
        } else {
            throw new UnsupportedOperationException("Tipo de amortización no soportado: " + type);
        }

        return details;
    }
}
