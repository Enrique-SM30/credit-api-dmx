package com.dmx.credit_api.infrastructure.adapter.in.web.dto;

import com.dmx.credit_api.domain.model.CreditStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(
        @NotNull(message = "Status is a must")
        CreditStatus status,
        String reason
) {

}
