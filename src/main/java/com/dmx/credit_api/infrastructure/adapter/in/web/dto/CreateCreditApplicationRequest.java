package com.dmx.credit_api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record CreateCreditApplicationRequest(
        String customerName,
        String customerEmail,
        String customerRfc,
        BigDecimal requestedAmount,
        String currency,
        Integer termMonths,
        BigDecimal annualInterestRate
) {}
