package com.ar.laboratory.paymentservice.payment.application.outbound.port;

/** Puerto de salida hacia el proveedor de pagos (p. ej. Stripe en modo test). */
public interface PaymentGatewayPort {
    /** Envía el cargo y devuelve la referencia del proveedor (no es la verdad final del pago). */
    String charge(long amount, String currency);
}
