package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditApplication;

public interface CreateCreditApplicationUseCase {
    CreditApplication execute(CreateCreditApplicationCommand command);
}
