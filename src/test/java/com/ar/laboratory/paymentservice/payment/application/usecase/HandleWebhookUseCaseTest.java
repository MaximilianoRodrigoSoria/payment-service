package com.ar.laboratory.paymentservice.payment.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.LedgerPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookEventPort;
import com.ar.laboratory.paymentservice.payment.domain.exception.PaymentNotFoundException;
import com.ar.laboratory.paymentservice.payment.domain.model.LedgerDirection;
import com.ar.laboratory.paymentservice.payment.domain.model.LedgerEntry;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import com.ar.laboratory.paymentservice.payment.domain.model.PaymentStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("HandleWebhookUseCase")
class HandleWebhookUseCaseTest {

    @Mock private PaymentRepositoryPort payments;
    @Mock private WebhookEventPort webhookEvents;
    @Mock private LedgerPort ledger;

    private Payment processing() {
        Payment p = Payment.create("k1", 1000, "usd", Instant.now());
        p.markProcessing("ref1", Instant.now());
        return p;
    }

    @Test
    @DisplayName("evento exitoso → SUCCEEDED y ledger balanceado (débito == crédito)")
    void succeeded() {
        when(webhookEvents.existsByEventId("evt1")).thenReturn(false);
        when(payments.findByProviderRef("ref1")).thenReturn(Optional.of(processing()));

        new HandleWebhookUseCase(payments, webhookEvents, ledger).execute("evt1", "ref1", true);

        ArgumentCaptor<List<LedgerEntry>> captor = ArgumentCaptor.forClass(List.class);
        verify(ledger).record(captor.capture());
        List<LedgerEntry> entries = captor.getValue();
        long debits =
                entries.stream()
                        .filter(e -> e.getDirection() == LedgerDirection.DEBIT)
                        .mapToLong(LedgerEntry::getAmount)
                        .sum();
        long credits =
                entries.stream()
                        .filter(e -> e.getDirection() == LedgerDirection.CREDIT)
                        .mapToLong(LedgerEntry::getAmount)
                        .sum();
        assertThat(debits).isEqualTo(credits).isEqualTo(1000L);
        verify(webhookEvents).markProcessed(anyString(), any());
    }

    @Test
    @DisplayName("evento ya procesado → idempotente, no toca nada")
    void idempotentEvent() {
        when(webhookEvents.existsByEventId("evt1")).thenReturn(true);
        new HandleWebhookUseCase(payments, webhookEvents, ledger).execute("evt1", "ref1", true);
        verify(payments, never()).save(any());
        verify(ledger, never()).record(any());
    }

    @Test
    @DisplayName("evento fallido → FAILED, sin ledger")
    void failed() {
        when(webhookEvents.existsByEventId("evt1")).thenReturn(false);
        Payment p = processing();
        when(payments.findByProviderRef("ref1")).thenReturn(Optional.of(p));

        new HandleWebhookUseCase(payments, webhookEvents, ledger).execute("evt1", "ref1", false);

        assertThat(p.getStatus()).isEqualTo(PaymentStatus.FAILED);
        verify(ledger, never()).record(any());
    }

    @Test
    @DisplayName("ref inexistente → PaymentNotFound")
    void notFound() {
        when(webhookEvents.existsByEventId("evt1")).thenReturn(false);
        when(payments.findByProviderRef("refX")).thenReturn(Optional.empty());
        assertThatThrownBy(
                        () ->
                                new HandleWebhookUseCase(payments, webhookEvents, ledger)
                                        .execute("evt1", "refX", true))
                .isInstanceOf(PaymentNotFoundException.class);
    }
}
