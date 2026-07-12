package com.ar.laboratory.paymentservice.payment.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Solicitud de cobro. El monto está en la mínima unidad de la moneda (centavos). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest {

    @Positive(message = "El monto debe ser positivo")
    private long amount;

    @Size(min = 3, max = 3, message = "La moneda debe ser un código ISO de 3 letras")
    private String currency;
}
