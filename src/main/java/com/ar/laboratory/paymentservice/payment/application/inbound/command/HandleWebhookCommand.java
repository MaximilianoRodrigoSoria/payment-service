package com.ar.laboratory.paymentservice.payment.application.inbound.command;

/** Puerto de entrada: procesar un evento de webhook (verdad del pago). */
public interface HandleWebhookCommand {
    void execute(String eventId, String providerRef, boolean succeeded);
}
