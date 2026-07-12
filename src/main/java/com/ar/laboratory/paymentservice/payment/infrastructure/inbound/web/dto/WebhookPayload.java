package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Payload del webhook del proveedor. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPayload {
    private String eventId;
    private String providerRef;
    private boolean succeeded;
}
