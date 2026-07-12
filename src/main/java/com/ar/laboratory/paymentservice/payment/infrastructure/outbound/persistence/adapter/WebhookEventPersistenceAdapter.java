package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.adapter;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.WebhookEventPort;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.WebhookEventEntity;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository.WebhookEventJpaRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Adaptador de idempotencia de eventos de webhook. */
@Component
@RequiredArgsConstructor
public class WebhookEventPersistenceAdapter implements WebhookEventPort {

    private final WebhookEventJpaRepository repository;

    @Override
    public boolean existsByEventId(String eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public void markProcessed(String eventId, Instant now) {
        repository.save(
                WebhookEventEntity.builder().eventId(eventId).processedAt(now).build());
    }
}
