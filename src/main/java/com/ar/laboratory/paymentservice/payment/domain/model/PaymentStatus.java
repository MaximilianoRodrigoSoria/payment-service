package com.ar.laboratory.paymentservice.payment.domain.model;

/** Estados de un pago. */
public enum PaymentStatus {
    REQUIRES_PAYMENT,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    REFUNDED
}
