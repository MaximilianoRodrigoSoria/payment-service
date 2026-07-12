package com.ar.laboratory.paymentservice.payment.application.outbound.port;

/** Puerto de salida para verificar la firma de un webhook. */
public interface WebhookVerifierPort {
    boolean verify(String payload, String signature);
}
