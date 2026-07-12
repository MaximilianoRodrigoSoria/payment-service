package com.ar.laboratory.paymentservice.payment.application.usecase;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.ChargeCommand;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentGatewayPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Inicia un cobro de forma idempotente: si la {@code idempotencyKey} ya se usó, devuelve el pago
 * existente (no cobra de nuevo). La confirmación real llega luego por webhook.
 */
@Slf4j
@RequiredArgsConstructor
public class ChargePaymentUseCase implements ChargeCommand {

    private final PaymentRepositoryPort payments;
    private final PaymentGatewayPort gateway;

    @Override
    public Payment execute(String idempotencyKey, long amount, String currency) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = payments.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                log.info("Idempotency-Key '{}' ya usada → devolviendo pago existente", idempotencyKey);
                return existing.get();
            }
        }
        Instant now = Instant.now();
        Payment payment = Payment.create(idempotencyKey, amount, currency, now);
        String providerRef = gateway.charge(amount, payment.getCurrency());
        payment.markProcessing(providerRef, now);
        Payment saved = payments.save(payment);
        log.info("Cobro iniciado id={} ref={} monto={}", saved.getId(), providerRef, amount);
        return saved;
    }
}
