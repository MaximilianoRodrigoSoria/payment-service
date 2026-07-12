package com.ar.laboratory.paymentservice.payment.domain.exception;

/** La firma del webhook no es válida. */
public class InvalidWebhookSignatureException extends RuntimeException {
    public InvalidWebhookSignatureException() {
        super("Firma de webhook inválida");
    }
}
