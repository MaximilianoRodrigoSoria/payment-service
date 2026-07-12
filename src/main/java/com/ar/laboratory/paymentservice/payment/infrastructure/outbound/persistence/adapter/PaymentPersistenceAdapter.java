package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.adapter;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.mapper.PaymentEntityMapper;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository.PaymentJpaRepository;
import com.ar.laboratory.paymentservice.shared.infrastructure.exception.InfrastructureException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Adaptador de persistencia de pagos. */
@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository repository;
    private final PaymentEntityMapper mapper;

    @Override
    public Payment save(Payment payment) {
        try {
            return mapper.toDomain(repository.save(mapper.toEntity(payment)));
        } catch (Exception e) {
            throw new InfrastructureException("Error guardando pago", e);
        }
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByProviderRef(String providerRef) {
        return repository.findByProviderRef(providerRef).map(mapper::toDomain);
    }
}
