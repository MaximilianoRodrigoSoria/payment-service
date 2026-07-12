package com.ar.laboratory.paymentservice.payment.application.outbound.port;

import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida para la persistencia de pagos. */
public interface PaymentRepositoryPort {
    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    Optional<Payment> findByProviderRef(String providerRef);
}
