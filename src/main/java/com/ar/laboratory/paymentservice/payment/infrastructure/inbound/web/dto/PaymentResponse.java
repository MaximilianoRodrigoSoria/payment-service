package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Vista de un pago. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private UUID id;
    private long amount;
    private String currency;
    private String status;
    private String providerRef;
    private Instant createdAt;
    private Instant updatedAt;
}
