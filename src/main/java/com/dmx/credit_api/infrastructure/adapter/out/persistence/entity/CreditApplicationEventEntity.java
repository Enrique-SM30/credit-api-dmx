package com.dmx.credit_api.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_application_events")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", nullable = false, columnDefinition = "uuid")
    private UUID applicationId;

    @Column(name = "event_type", nullable = false, length = 40)
    private String eventType;

    @Column(name = "old_status", length = 20)
    private String oldStatus;

    @Column(name = "new_status", length = 20)
    private String newStatus;

    @Column(name = "occurred_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime occurredAt;

    @Column(name = "actor", length = 120)
    private String actor;

    @Column(name = "payload", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;
}
