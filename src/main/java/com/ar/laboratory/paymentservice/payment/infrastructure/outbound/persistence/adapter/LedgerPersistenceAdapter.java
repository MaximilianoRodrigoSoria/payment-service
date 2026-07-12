package com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.adapter;

import com.ar.laboratory.paymentservice.payment.application.outbound.port.LedgerPort;
import com.ar.laboratory.paymentservice.payment.domain.model.LedgerEntry;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.entity.LedgerEntryEntity;
import com.ar.laboratory.paymentservice.payment.infrastructure.outbound.persistence.repository.LedgerEntryJpaRepository;
import com.ar.laboratory.paymentservice.shared.infrastructure.exception.InfrastructureException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Adaptador de persistencia del ledger de doble entrada. */
@Component
@RequiredArgsConstructor
public class LedgerPersistenceAdapter implements LedgerPort {

    private final LedgerEntryJpaRepository repository;

    @Override
    @Transactional
    public void record(List<LedgerEntry> entries) {
        try {
            repository.saveAll(
                    entries.stream()
                            .map(
                                    e ->
                                            LedgerEntryEntity.builder()
                                                    .id(e.getId())
                                                    .paymentId(e.getPaymentId())
                                                    .account(e.getAccount())
                                                    .direction(e.getDirection().name())
                                                    .amount(e.getAmount())
                                                    .currency(e.getCurrency())
                                                    .createdAt(e.getCreatedAt())
                                                    .build())
                            .toList());
        } catch (Exception e) {
            throw new InfrastructureException("Error asentando en el ledger", e);
        }
    }
}
