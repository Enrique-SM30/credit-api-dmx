CREATE TABLE credit_application_events (
    id BIGSERIAL PRIMARY KEY,
    application_id UUID NOT NULL REFERENCES credit_applications(id),
    event_type VARCHAR(40) NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20),
    payload JSONB,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actor VARCHAR(120)
);
CREATE INDEX idx_events_application ON credit_application_events(application_id);
CREATE INDEX idx_events_occurred_at ON credit_application_events(occurred_at DESC);
