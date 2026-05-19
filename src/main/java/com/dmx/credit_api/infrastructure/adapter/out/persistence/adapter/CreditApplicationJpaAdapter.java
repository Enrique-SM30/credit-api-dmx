package com.dmx.credit_api.infrastructure.adapter.out.persistence.adapter;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.model.PageRequest;
import com.dmx.credit_api.domain.model.PageResult;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.mapper.CreditApplicationPersistenceMapper;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.repository.CreditApplicationJpaRepository;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.repository.CreditApplicationSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<CreditApplication> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public PageResult<CreditApplication> findAll(
            CreditStatus status,
            String customerRfc,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            PageRequest pageRequest
    ) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.pageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<CreditApplicationEntity> specifications =
                Specification.allOf(CreditApplicationSpecifications.hasStatus(status))
                        .and(CreditApplicationSpecifications.hasRfc(customerRfc))
                        .and(CreditApplicationSpecifications.minAmount(minAmount))
                        .and(CreditApplicationSpecifications.maxAmount(maxAmount));

        Page<CreditApplicationEntity> page = jpaRepository.findAll(specifications, pageable);

        return new PageResult<>(
                page.getContent().stream().map(mapper::toDomainEntity).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
