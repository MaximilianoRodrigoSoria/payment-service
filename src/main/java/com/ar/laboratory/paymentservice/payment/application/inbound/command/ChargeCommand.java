package com.ar.laboratory.paymentservice.payment.application.inbound.command;

import com.ar.laboratory.paymentservice.payment.domain.model.Payment;

/** Puerto de entrada: iniciar un cobro idempotente. */
public interface ChargeCommand {
    Payment execute(String idempotencyKey, long amount, String currency);
}
