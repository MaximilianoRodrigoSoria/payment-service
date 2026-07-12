package com.ar.laboratory.paymentservice.payment.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Asiento de un libro de doble entrada asociado a un pago. */
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntry {

    private UUID id;
    private UUID paymentId;
    private String account;
    private LedgerDirection direction;
    private long amount;
    private String currency;
    private Instant createdAt;

    public static LedgerEntry of(
            UUID paymentId,
            String account,
            LedgerDirection direction,
            long amount,
            String currency,
            Instant now) {
        return LedgerEntry.builder()
                .id(UUID.randomUUID())
                .paymentId(paymentId)
                .account(account)
                .direction(direction)
                .amount(amount)
                .currency(currency)
                .createdAt(now)
                .build();
    }
}
