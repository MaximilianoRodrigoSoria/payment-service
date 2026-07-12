package com.ar.laboratory.paymentservice.payment.application.usecase;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.GetPaymentCommand;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.domain.exception.PaymentNotFoundException;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/** Consulta un pago por id. POJO sin framework. */
@RequiredArgsConstructor
public class GetPaymentUseCase implements GetPaymentCommand {

    private final PaymentRepositoryPort payments;

    @Override
    public Payment execute(UUID id) {
        return payments
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pago no encontrado: " + id));
    }
}
