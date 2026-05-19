package com.dmx.credit_api.domain.port.out;

import com.dmx.credit_api.domain.model.CreditApplication;

import java.util.Optional;
import java.util.UUID;

public interface CreditApplicationRepository {
    CreditApplication save(CreditApplication application);

    Optional<CreditApplication> findById(UUID id);
}
