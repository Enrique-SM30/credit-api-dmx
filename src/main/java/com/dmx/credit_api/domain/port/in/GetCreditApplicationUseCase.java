package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditApplication;

import java.util.UUID;

public interface GetCreditApplicationUseCase {
    CreditApplication execute(UUID id);
}
