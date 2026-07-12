package com.ar.laboratory.paymentservice.payment.application.outbound.port;

import com.ar.laboratory.paymentservice.payment.domain.model.LedgerEntry;
import java.util.List;

/** Puerto de salida para el libro de doble entrada. */
public interface LedgerPort {
    void record(List<LedgerEntry> entries);
}
