package com.ar.laboratory.paymentservice.payment.application.inbound.command;

import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import java.util.UUID;

/** Puerto de entrada: consultar un pago. */
public interface GetPaymentCommand {
    Payment execute(UUID id);
}
