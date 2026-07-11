<!-- banner-badges -->
<p align="center">
  <a href="https://www.linkedin.com/in/soriamaximilianorodrigo/" target="_blank" rel="noopener noreferrer">
    <img width="100%" src="docs/img/banner.gif" alt="Payment Service — Maximiliano Rodrigo Soria">
  </a>
</p>

<p align="center">
  <a href="LICENSE"><img src="https://img.shields.io/github/license/MaximilianoRodrigoSoria/payment-service?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="License"></a>
  <img src="https://img.shields.io/github/last-commit/MaximilianoRodrigoSoria/payment-service?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="Last commit">
  <img src="https://img.shields.io/github/repo-size/MaximilianoRodrigoSoria/payment-service?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="Repo size">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-06C69C?style=flat-square&labelColor=1A1C1F&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Stripe-•-06C69C?style=flat-square&labelColor=1A1C1F&logo=stripe&logoColor=white" alt="Stripe">
  <img src="https://img.shields.io/badge/PostgreSQL-•-06C69C?style=flat-square&labelColor=1A1C1F&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Ledger-•-06C69C?style=flat-square&labelColor=1A1C1F" alt="Ledger">
  <img src="https://img.shields.io/badge/Resilience4j-•-06C69C?style=flat-square&labelColor=1A1C1F" alt="Resilience4j">
</p>

# Payment Service

Pasarela de pagos con facturacion centrada en la correccion financiera: cobros idempotentes con Stripe, webhooks firmados como fuente de verdad, ledger de doble entrada y reconciliacion.

> Proyecto de portafolio backend. Sigue el estandar de **arquitectura hexagonal (Ports & Adapters)**, Java 21 y Spring Boot, con quality gates (Spotless, Checkstyle, PMD, SpotBugs, ArchUnit), testing con Testcontainers y observabilidad (Micrometer + Prometheus).

## Caracteristicas

- Cobro idempotente: la misma `Idempotency-Key` no duplica el cargo
- Integracion con Stripe (modo test)
- Webhook con verificacion de firma como fuente de verdad
- Idempotencia de webhooks por `event.id`
- Ledger de doble entrada con asientos inmutables
- Emision de recibos / facturas
- Reconciliacion periodica contra el proveedor
- Nunca almacena datos de tarjeta (los maneja Stripe)

## Stack

Java 21 · Stripe · PostgreSQL · Ledger · Resilience4j · Gradle · Flyway · Docker · JUnit 5 · Testcontainers

## Arquitectura

Organizado por **feature** en capas `domain -> application -> infrastructure`, con la regla de dependencia verificada por ArchUnit. La logica de negocio (dominio y casos de uso) no depende de framework ni de infraestructura; los adaptadores (web, persistencia, mensajeria) implementan puertos definidos por la aplicacion.

## Estado

🚧 En planificacion / arranque. El diseno detallado (epicas, historias y criterios de aceptacion) vive en el plan del portafolio.

---

<p align="center">
  <strong>Maximiliano Rodrigo Soria</strong><br>
  <a href="https://www.linkedin.com/in/soriamaximilianorodrigo/">LinkedIn</a> · <a href="mailto:maximilianorodrigosoria@gmail.com">maximilianorodrigosoria@gmail.com</a>
</p>
