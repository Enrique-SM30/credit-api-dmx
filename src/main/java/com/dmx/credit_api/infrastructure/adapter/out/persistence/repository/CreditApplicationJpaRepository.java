package com.dmx.credit_api.infrastructure.adapter.out.persistence.repository;

import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CreditApplicationJpaRepository extends JpaRepository<CreditApplicationEntity, UUID>, JpaSpecificationExecutor<CreditApplicationEntity> {
}
