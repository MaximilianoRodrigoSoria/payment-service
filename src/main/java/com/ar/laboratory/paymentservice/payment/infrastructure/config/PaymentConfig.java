package com.ar.laboratory.paymentservice.payment.infrastructure.config;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.ChargeCommand;
import com.ar.laboratory.paymentservice.payment.application.inbound.command.GetPaymentCommand;
import com.ar.laboratory.paymentservice.payment.application.inbound.command.HandleWebhookCommand;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.LedgerPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentGatewayPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.PaymentRepositoryPort;
import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookEventPort;
import com.ar.laboratory.paymentservice.payment.application.usecase.ChargePaymentUseCase;
import com.ar.laboratory.paymentservice.payment.application.usecase.GetPaymentUseCase;
import com.ar.laboratory.paymentservice.payment.application.usecase.HandleWebhookUseCase;
import com.ar.laboratory.paymentservice.payment.infrastructure.security.WebhookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wiring de los casos de uso de pagos. */
@Configuration
@EnableConfigurationProperties(WebhookProperties.class)
public class PaymentConfig {

    @Bean
    public ChargeCommand chargeCommand(PaymentRepositoryPort payments, PaymentGatewayPort gateway) {
        return new ChargePaymentUseCase(payments, gateway);
    }

    @Bean
    public HandleWebhookCommand handleWebhookCommand(
            PaymentRepositoryPort payments, WebhookEventPort webhookEvents, LedgerPort ledger) {
        return new HandleWebhookUseCase(payments, webhookEvents, ledger);
    }

    @Bean
    public GetPaymentCommand getPaymentCommand(PaymentRepositoryPort payments) {
        return new GetPaymentUseCase(payments);
    }
}
