package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository;

import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.LedgerEntryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA de asientos del ledger. */
public interface LedgerEntryJpaRepository extends JpaRepository<LedgerEntryEntity, UUID> {}
