package com.ar.laboratory.paymentservice.payment.infrastructure.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuración de pagos ({@code app.payment.*}). */
@Data
@ConfigurationProperties(prefix = "app.payment")
public class WebhookProperties {
    /** Secret para verificar la firma HMAC de los webhooks. */
    private String webhookSecret = "whsec_test_secret_change_me";
}
