package com.dmx.credit_api.infrastructure.adapter.out.persistence.adapter;

import com.dmx.credit_api.domain.model.CreditApplicationEvent;
import com.dmx.credit_api.domain.port.out.CreditApplicationEventRepository;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEventEntity;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.repository.CreditApplicationEventJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationEventJpaAdapter implements CreditApplicationEventRepository {

    private final CreditApplicationEventJpaRepository jpaRepository;

    public CreditApplicationEventJpaAdapter(CreditApplicationEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(CreditApplicationEvent event) {
        CreditApplicationEventEntity entity = CreditApplicationEventEntity.builder()
                .applicationId(event.getApplicationId())
                .eventType(event.getEventType())
                .oldStatus(event.getOldStatus() != null ? event.getOldStatus().name() : null)
                .newStatus(event.getNewStatus() != null ? event.getNewStatus().name() : null)
                .occurredAt(event.getOccurredAt())
                .actor(event.getActor())
                .payload(event.getPayload())
                .build();

        jpaRepository.save(entity);
    }
}
