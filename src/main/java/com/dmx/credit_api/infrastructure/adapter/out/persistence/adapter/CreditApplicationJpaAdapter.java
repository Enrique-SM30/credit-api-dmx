package com.dmx.credit_api.infrastructure.adapter.out.persistence.adapter;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.mapper.CreditApplicationPersistenceMapper;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.repository.CreditApplicationJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditApplicationJpaAdapter implements CreditApplicationRepository {
    private final CreditApplicationJpaRepository jpaRepository;
    private final CreditApplicationPersistenceMapper mapper;

    public CreditApplicationJpaAdapter(CreditApplicationJpaRepository jpaRepository, CreditApplicationPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public CreditApplication save(CreditApplication application) {
        CreditApplicationEntity saved = jpaRepository.save(mapper.toEntity(application));
        log.info(String.valueOf(saved));
        return mapper.toDomainEntity(saved);
    }
}
