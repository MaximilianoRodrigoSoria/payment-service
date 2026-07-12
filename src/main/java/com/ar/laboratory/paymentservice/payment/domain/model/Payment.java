package com.ar.laboratory.paymentservice.payment.domain.model;

import com.ar.laboratory.paymentservice.payment.domain.exception.InvalidPaymentTransitionException;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Intento de pago. La respuesta síncrona no es la verdad final: el estado definitivo se confirma por
 * webhook. Los montos se manejan en la mínima unidad de la moneda (p. ej. centavos).
 */
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private UUID id;
    private String idempotencyKey;
    private long amount;
    private String currency;
    private PaymentStatus status;
    private String providerRef;
    private String lastError;
    private Instant createdAt;
    private Instant updatedAt;

    public static Payment create(String idempotencyKey, long amount, String currency, Instant now) {
        return Payment.builder()
                .id(UUID.randomUUID())
                .idempotencyKey(idempotencyKey)
                .amount(amount)
                .currency(currency == null ? "usd" : currency.toLowerCase())
                .status(PaymentStatus.REQUIRES_PAYMENT)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /** REQUIRES_PAYMENT → PROCESSING tras enviar el cargo al proveedor. */
    public void markProcessing(String providerRef, Instant now) {
        require(PaymentStatus.REQUIRES_PAYMENT, PaymentStatus.PROCESSING);
        this.providerRef = providerRef;
        this.status = PaymentStatus.PROCESSING;
        this.updatedAt = now;
    }

    /** PROCESSING → SUCCEEDED (confirmado por webhook). Idempotente si ya está SUCCEEDED. */
    public void markSucceeded(Instant now) {
        if (status == PaymentStatus.SUCCEEDED) {
            return;
        }
        require(PaymentStatus.PROCESSING, PaymentStatus.SUCCEEDED);
        this.status = PaymentStatus.SUCCEEDED;
        this.updatedAt = now;
    }

    /** PROCESSING → FAILED. */
    public void markFailed(String reason, Instant now) {
        if (status == PaymentStatus.FAILED) {
            return;
        }
        require(PaymentStatus.PROCESSING, PaymentStatus.FAILED);
        this.status = PaymentStatus.FAILED;
        this.lastError = reason;
        this.updatedAt = now;
    }

    private void require(PaymentStatus from, PaymentStatus to) {
        if (this.status != from) {
            throw new InvalidPaymentTransitionException(this.status, to);
        }
    }
}
