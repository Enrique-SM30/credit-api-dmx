package com.dmx.credit_api.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreditApplicationEvent {

    private final UUID applicationId;
    private final String eventType;
    private final CreditStatus oldStatus;
    private final CreditStatus newStatus;
    private final String actor;
    private final String payload;
    private final OffsetDateTime occurredAt;

    public CreditApplicationEvent(
            UUID applicationId,
            String eventType,
            CreditStatus oldStatus,
            CreditStatus newStatus,
            String actor,
            String payload
    ) {
        this.applicationId = applicationId;
        this.eventType = eventType;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.actor = actor;
        this.payload = payload;
        this.occurredAt = OffsetDateTime.now();
    }

    public UUID getApplicationId()   { return applicationId; }
    public String getEventType()     { return eventType; }
    public CreditStatus getOldStatus() { return oldStatus; }
    public CreditStatus getNewStatus() { return newStatus; }
    public String getActor()         { return actor; }
    public String getPayload() { return payload; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
}
