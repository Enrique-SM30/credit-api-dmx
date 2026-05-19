package com.dmx.credit_api.domain.port.out;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.model.PageRequest;
import com.dmx.credit_api.domain.model.PageResult;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CreditApplicationRepository {
    CreditApplication save(CreditApplication application);

    Optional<CreditApplication> findById(UUID id);

    PageResult<CreditApplication> findAll(CreditStatus status, String customerRfc, BigDecimal minAmount, BigDecimal maxAmount, PageRequest pageRequest);
}
