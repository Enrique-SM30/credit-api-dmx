package com.dmx.credit_api.domain.port.out;

import com.dmx.credit_api.domain.model.CreditApplication;

public interface CreditApplicationRepository {
    CreditApplication save(CreditApplication application);
}
