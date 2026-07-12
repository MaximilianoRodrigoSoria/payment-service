package com.ar.laboratory.paymentservice.payment.domain.exception;

import com.ar.laboratory.paymentservice.payment.domain.model.PaymentStatus;

/** Transición de estado de pago inválida. */
public class InvalidPaymentTransitionException extends RuntimeException {
    public InvalidPaymentTransitionException(PaymentStatus from, PaymentStatus to) {
        super("Transición de pago inválida: " + from + " → " + to);
    }
}
