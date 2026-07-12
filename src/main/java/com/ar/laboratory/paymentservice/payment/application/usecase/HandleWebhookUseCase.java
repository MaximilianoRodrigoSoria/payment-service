package com.ar.laboratory.paymentservice.payment.application.usecase;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.HandleWebhookCommand;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.LedgerPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookEventPort;
import com.ar.laboratory.paymentservice.payment.domain.exception.PaymentNotFoundException;
import com.ar.laboratory.paymentservice.payment.domain.model.LedgerDirection;
import com.ar.laboratory.paymentservice.payment.domain.model.LedgerEntry;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Procesa un evento de webhook (fuente de verdad del pago). Idempotente por {@code eventId}. Cuando
 * el pago se confirma, asienta el movimiento en el libro de doble entrada (débito de caja y crédito
 * de ingresos por el mismo monto, sumando cero).
 */
@Slf4j
@RequiredArgsConstructor
public class HandleWebhookUseCase implements HandleWebhookCommand {

    private static final String ACCOUNT_CASH = "cash";
    private static final String ACCOUNT_REVENUE = "revenue";

    private final PaymentRepositoryPort payments;
    private final WebhookEventPort webhookEvents;
    private final LedgerPort ledger;

    @Override
    public void execute(String eventId, String providerRef, boolean succeeded) {
        if (webhookEvents.existsByEventId(eventId)) {
            log.info("Evento {} ya procesado → ignorado (idempotencia de webhook)", eventId);
            return;
        }
        Instant now = Instant.now();
        Payment payment =
                payments
                        .findByProviderRef(providerRef)
                        .orElseThrow(
                                () ->
                                        new PaymentNotFoundException(
                                                "Pago no encontrado para ref: " + providerRef));

        if (succeeded) {
            payment.markSucceeded(now);
            ledger.record(
                    List.of(
                            LedgerEntry.of(
                                    payment.getId(),
                                    ACCOUNT_CASH,
                                    LedgerDirection.DEBIT,
                                    payment.getAmount(),
                                    payment.getCurrency(),
                                    now),
                            LedgerEntry.of(
                                    payment.getId(),
                                    ACCOUNT_REVENUE,
                                    LedgerDirection.CREDIT,
                                    payment.getAmount(),
                                    payment.getCurrency(),
                                    now)));
            log.info("Pago {} SUCCEEDED y asentado en el ledger", payment.getId());
        } else {
            payment.markFailed("Rechazado por el proveedor", now);
            log.warn("Pago {} FAILED", payment.getId());
        }
        payments.save(payment);
        webhookEvents.markProcessed(eventId, now);
    }
}
