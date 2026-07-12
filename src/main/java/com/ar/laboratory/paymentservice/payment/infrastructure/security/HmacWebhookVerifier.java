package com.ar.laboratory.paymentservice.payment.infrastructure.security;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookVerifierPort;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Verifica la firma del webhook con HMAC-SHA256 sobre el payload crudo (comparación de tiempo constante). */
@Slf4j
@Component
@RequiredArgsConstructor
public class HmacWebhookVerifier implements WebhookVerifierPort {

    private final WebhookProperties properties;

    @Override
    public boolean verify(String payload, String signature) {
        if (payload == null || signature == null) {
            return false;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(
                    new SecretKeySpec(
                            properties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"));
            byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(raw.length * 2);
            for (byte b : raw) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return MessageDigest.isEqual(
                    sb.toString().getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.warn("No se pudo verificar la firma: {}", e.getMessage());
            return false;
        }
    }
}
