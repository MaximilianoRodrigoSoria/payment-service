CREATE TABLE IF NOT EXISTS app.payments (
    id              UUID        PRIMARY KEY,
    idempotency_key VARCHAR(200) UNIQUE,
    amount          BIGINT      NOT NULL,
    currency        VARCHAR(3)  NOT NULL,
    status          VARCHAR(20) NOT NULL,
    provider_ref    VARCHAR(200) UNIQUE,
    last_error      TEXT,
    created_at      TIMESTAMP   NOT NULL,
    updated_at      TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS app.ledger_entries (
    id         UUID        PRIMARY KEY,
    payment_id UUID        NOT NULL REFERENCES app.payments(id) ON DELETE CASCADE,
    account    VARCHAR(40) NOT NULL,
    direction  VARCHAR(10) NOT NULL,
    amount     BIGINT      NOT NULL,
    currency   VARCHAR(3)  NOT NULL,
    created_at TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS app.webhook_events (
    event_id     VARCHAR(200) PRIMARY KEY,
    processed_at TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_ledger_payment ON app.ledger_entries(payment_id);

COMMENT ON TABLE app.payments        IS 'Pagos (intentos). La verdad final llega por webhook';
COMMENT ON TABLE app.ledger_entries  IS 'Libro de doble entrada: debito + credito por movimiento';
COMMENT ON TABLE app.webhook_events  IS 'Eventos de webhook procesados (idempotencia)';
