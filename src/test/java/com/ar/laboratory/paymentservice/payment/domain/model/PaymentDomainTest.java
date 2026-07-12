package com.ar.laboratory.paymentservice.payment.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ar.laboratory.paymentservice.payment.domain.exception.InvalidPaymentTransitionException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Dominio de pagos")
class PaymentDomainTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    @DisplayName("ciclo feliz: REQUIRES_PAYMENT → PROCESSING → SUCCEEDED")
    void happyPath() {
        Payment p = Payment.create("k1", 1000, "USD", NOW);
        assertThat(p.getStatus()).isEqualTo(PaymentStatus.REQUIRES_PAYMENT);
        assertThat(p.getCurrency()).isEqualTo("usd");
        p.markProcessing("ref", NOW);
        assertThat(p.getStatus()).isEqualTo(PaymentStatus.PROCESSING);
        p.markSucceeded(NOW);
        assertThat(p.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
    }

    @Test
    @DisplayName("markSucceeded es idempotente")
    void succeedIdempotent() {
        Payment p = Payment.create("k1", 1000, "usd", NOW);
        p.markProcessing("ref", NOW);
        p.markSucceeded(NOW);
        p.markSucceeded(NOW); // no lanza
        assertThat(p.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
    }

    @Test
    @DisplayName("no se puede confirmar sin haber pasado a PROCESSING")
    void invalidTransition() {
        Payment p = Payment.create("k1", 1000, "usd", NOW);
        assertThatThrownBy(() -> p.markSucceeded(NOW))
                .isInstanceOf(InvalidPaymentTransitionException.class);
    }
}
