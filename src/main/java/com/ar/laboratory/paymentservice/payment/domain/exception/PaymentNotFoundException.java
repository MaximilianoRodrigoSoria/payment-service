package com.ar.laboratory.paymentservice.payment.domain.exception;

/** No se encontró el pago. */
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
