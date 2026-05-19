package com.dmx.credit_api.domain.port.in;

import java.math.BigDecimal;

public record CreateCreditApplicationCommand(
        String customerName,
        String customerEmail,
        String customerRfc,
        BigDecimal requestedAmount,
        String currency,
        int termMonths,
        BigDecimal annualInterestRate
) {
}