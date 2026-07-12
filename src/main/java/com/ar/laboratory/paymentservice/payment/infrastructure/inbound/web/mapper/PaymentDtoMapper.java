package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.mapper;

import com.ar.laboratory.paymentservice.payment.domain.model.Payment;
import com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto.PaymentResponse;
import org.springframework.stereotype.Component;

/** Conversión Payment → DTO. */
@Component
public class PaymentDtoMapper {
    public PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .amount(p.getAmount())
                .currency(p.getCurrency())
                .status(p.getStatus() == null ? null : p.getStatus().name())
                .providerRef(p.getProviderRef())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
