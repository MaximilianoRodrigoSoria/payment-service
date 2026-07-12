package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.controller;

import com.ar.laboratory.paymentservice.payment.application.inbound.command.ChargeCommand;
import com.ar.laboratory.paymentservice.payment.application.inbound.command.GetPaymentCommand;
import com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto.ChargeRequest;
import com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto.PaymentResponse;
import com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.mapper.PaymentDtoMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API de pagos. El cobro es idempotente por la cabecera {@code Idempotency-Key}. */
@Tag(name = "Payments", description = "Cobros idempotentes")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@RateLimiter(name = "payments-api")
public class PaymentController {

    private final ChargeCommand chargeCommand;
    private final GetPaymentCommand getPaymentCommand;
    private final PaymentDtoMapper mapper;

    @PostMapping
    public ResponseEntity<PaymentResponse> charge(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody ChargeRequest request) {
        var payment =
                chargeCommand.execute(idempotencyKey, request.getAmount(), request.getCurrency());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.toResponse(payment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(getPaymentCommand.execute(id)));
    }
}
