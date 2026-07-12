package com.ar.laboratory.paymentservice.payment.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentGatewayPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import com.ar.laboratory.paymentservice.payment.domain.model.PaymentStatus;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChargePaymentUseCase")
class ChargePaymentUseCaseTest {

    @Mock private PaymentRepositoryPort payments;
    @Mock private PaymentGatewayPort gateway;

    @Test
    @DisplayName("nuevo cobro: cobra en el proveedor y queda PROCESSING")
    void newCharge() {
        when(payments.findByIdempotencyKey("k1")).thenReturn(Optional.empty());
        when(gateway.charge(anyLong(), anyString())).thenReturn("ch_test_1");
        when(payments.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment p = new ChargePaymentUseCase(payments, gateway).execute("k1", 1000, "usd");

        assertThat(p.getStatus()).isEqualTo(PaymentStatus.PROCESSING);
        assertThat(p.getProviderRef()).isEqualTo("ch_test_1");
    }

    @Test
    @DisplayName("idempotencia: clave ya usada → devuelve el existente sin cobrar")
    void idempotent() {
        Payment existing = Payment.create("k1", 1000, "usd", java.time.Instant.now());
        when(payments.findByIdempotencyKey("k1")).thenReturn(Optional.of(existing));

        Payment p = new ChargePaymentUseCase(payments, gateway).execute("k1", 1000, "usd");

        assertThat(p).isEqualTo(existing);
        verify(gateway, never()).charge(anyLong(), anyString());
        verify(payments, never()).save(any());
    }
}
