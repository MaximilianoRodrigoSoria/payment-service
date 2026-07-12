package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.mapper;

import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import com.ar.laboratory.paymentservice.payment.domain.model.PaymentStatus;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

/** Conversión PaymentEntity ↔ Payment. */
@Component
public class PaymentEntityMapper {

    public Payment toDomain(PaymentEntity e) {
        if (e == null) {
            return null;
        }
        return Payment.builder()
                .id(e.getId())
                .idempotencyKey(e.getIdempotencyKey())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .status(e.getStatus() == null ? null : PaymentStatus.valueOf(e.getStatus()))
                .providerRef(e.getProviderRef())
                .lastError(e.getLastError())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public PaymentEntity toEntity(Payment p) {
        return PaymentEntity.builder()
                .id(p.getId())
                .idempotencyKey(p.getIdempotencyKey())
                .amount(p.getAmount())
                .currency(p.getCurrency())
                .status(p.getStatus() == null ? null : p.getStatus().name())
                .providerRef(p.getProviderRef())
                .lastError(p.getLastError())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
