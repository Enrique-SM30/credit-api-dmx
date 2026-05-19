package com.dmx.credit_api.infrastructure.adapter.in.web.dto;

import com.dmx.credit_api.domain.model.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreditApplicationResponse(UUID id, String customerName, String customerEmail, String customerRfc, BigDecimal requestedAmount, String currency, int termMonths, BigDecimal annualInterestRate, BigDecimal monthlyPayment, BigDecimal totalToPay, BigDecimal amountUsd, BigDecimal amountEur, LocalDate exchangeRateDate, CreditStatus status, String statusReason, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
