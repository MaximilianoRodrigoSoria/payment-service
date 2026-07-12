package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.gateway;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentGatewayPort;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adaptador del proveedor de pagos en modo test. En un despliegue real llamaría a la API de Stripe
 * (crear PaymentIntent). Aquí simula el cargo y devuelve una referencia; la verdad del pago llega
 * luego por webhook.
 */
@Slf4j
@Component
public class StripePaymentGatewayAdapter implements PaymentGatewayPort {

    @Override
    public String charge(long amount, String currency) {
        String ref = "ch_test_" + UUID.randomUUID().toString().replace("-", "");
        log.info("[stripe-test] cargo de {} {} -> {}", amount, currency, ref);
        return ref;
    }
}
