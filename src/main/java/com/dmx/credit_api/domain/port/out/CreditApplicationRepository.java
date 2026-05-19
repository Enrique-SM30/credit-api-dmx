package com.dmx.credit_api.domain.port.out;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CreditApplicationRepository {
    CreditApplication save(CreditApplication application);

    Optional<CreditApplication> findById(UUID id);

    Page<CreditApplication> findAll(CreditStatus status, String customerRfc, BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
}
