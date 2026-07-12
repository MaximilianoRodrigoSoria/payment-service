package com.ar.laboratory.paymentservice.payment.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Tests de integración del flujo de pago con firma HMAC real de webhook. */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("Payment flow - Integration Tests")
class PaymentFlowIT {

    private static final String BASE = "/payment-service/api/v1/payments";
    private static final String WEBHOOK = "/payment-service/webhooks/stripe";
    private static final String SECRET = "whsec_test_secret_change_me";

    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @LocalServerPort private int port;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    private static String sign(String payload) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    private JsonNode charge(String key) {
        return client.post()
                .uri(BASE)
                .header("Idempotency-Key", key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("amount", 1500, "currency", "usd"))
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @DisplayName("cobro idempotente → webhook firmado → pago SUCCEEDED")
    void fullFlow() throws Exception {
        String key = "idem-" + java.util.UUID.randomUUID();
        JsonNode p1 = charge(key);
        JsonNode p2 = charge(key); // misma clave → mismo pago
        assertThat(p2.get("id").asText()).isEqualTo(p1.get("id").asText());

        String providerRef = p1.get("providerRef").asText();
        String id = p1.get("id").asText();
        String payload =
                "{\"eventId\":\"evt_" + key + "\",\"providerRef\":\"" + providerRef
                        + "\",\"succeeded\":true}";

        client.post()
                .uri(WEBHOOK)
                .header("Stripe-Signature", sign(payload))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus()
                .isOk();

        client.get()
                .uri(BASE + "/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("SUCCEEDED");
    }

    @Test
    @DisplayName("webhook con firma inválida → 401")
    void invalidSignature() {
        String payload = "{\"eventId\":\"evt_x\",\"providerRef\":\"ref\",\"succeeded\":true}";
        client.post()
                .uri(WEBHOOK)
                .header("Stripe-Signature", "firma-invalida")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
