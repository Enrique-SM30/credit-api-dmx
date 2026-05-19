package com.dmx.credit_api.infrastructure.adapter.out.persistence.repository;

import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditApplicationEventJpaRepository
        extends JpaRepository<CreditApplicationEventEntity, Long> {
}
