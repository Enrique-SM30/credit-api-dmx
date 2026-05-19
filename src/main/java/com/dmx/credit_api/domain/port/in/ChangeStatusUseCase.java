package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;

import java.util.UUID;

public interface ChangeStatusUseCase {
    CreditApplication execute(UUID id, CreditStatus newStatus, String reason);
}
