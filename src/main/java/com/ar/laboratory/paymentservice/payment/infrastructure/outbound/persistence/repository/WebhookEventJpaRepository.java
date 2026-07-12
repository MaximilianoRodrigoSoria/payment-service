package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository;

import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.WebhookEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA de eventos de webhook. */
public interface WebhookEventJpaRepository extends JpaRepository<WebhookEventEntity, String> {}
