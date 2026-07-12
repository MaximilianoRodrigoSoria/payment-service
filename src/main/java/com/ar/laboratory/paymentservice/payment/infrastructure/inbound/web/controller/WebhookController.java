package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.controller;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.HandleWebhookCommand;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookVerifierPort;
import com.ar.laboratory.paymentservice.payment.domain.exception.InvalidWebhookSignatureException;
import com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto.WebhookPayload;
import com.ar.laboratory.paymentservice.shared.infrastructure.util.JsonHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recibe los webhooks del proveedor. Verifica la firma HMAC sobre el payload crudo antes de
 * procesarlo; es la fuente de verdad del estado del pago.
 */
@Tag(name = "Webhooks", description = "Webhooks del proveedor de pagos")
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookVerifierPort verifier;
    private final HandleWebhookCommand handleWebhookCommand;
    private final JsonHandler jsonHandler;

    @PostMapping("/stripe")
    public ResponseEntity<Void> stripe(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        if (!verifier.verify(payload, signature)) {
            throw new InvalidWebhookSignatureException();
        }
        WebhookPayload event = jsonHandler.fromJson(payload, WebhookPayload.class);
        handleWebhookCommand.execute(
                event.getEventId(), event.getProviderRef(), event.isSucceeded());
        return ResponseEntity.ok().build();
    }
}
