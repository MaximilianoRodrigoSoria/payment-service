package com.ar.laboratory.paymentservice.payment.application.outbound.port;

import java.time.Instant;

/** Puerto de salida para la idempotencia de eventos de webhook. */
public interface WebhookEventPort {
    boolean existsByEventId(String eventId);

    void markProcessed(String eventId, Instant now);
}
