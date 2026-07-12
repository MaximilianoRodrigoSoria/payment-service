package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository;

import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.PaymentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA de pagos. */
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);

    Optional<PaymentEntity> findByProviderRef(String providerRef);
}
