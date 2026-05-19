package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditStatus;

import java.math.BigDecimal;

public record GetCreditApplicationsListQuery(CreditStatus status, String customerRfc, BigDecimal minAmount, BigDecimal maxAmount) {
}
