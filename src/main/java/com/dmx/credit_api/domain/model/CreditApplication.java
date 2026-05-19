package com.dmx.credit_api.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class CreditApplication {
    private final UUID id;
    private final String customerName;

    private final String customerEmail;
    private final String customerRfc;
    private final BigDecimal requestedAmount;
    private final String currency;
    private final int termMonths;
    private final BigDecimal annualInterestRate;
    private final BigDecimal monthlyPayment;
    private final BigDecimal totalToPay;

    private BigDecimal amountUsd;
    private BigDecimal amountEur;
    private LocalDate exchangeRateDate;

    private CreditStatus status;
    private String statusReason;

    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CreditApplication(UUID id, String customerName, String customerEmail, String customerRfc,
                             BigDecimal requestedAmount, String currency, int termMonths,
                             BigDecimal annualInterestRate, BigDecimal monthlyPayment, BigDecimal totalToPay,
                             BigDecimal amountUsd, BigDecimal amountEur, LocalDate exchangeRateDate,
                             CreditStatus status, String statusReason, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerRfc = customerRfc;
        this.requestedAmount = requestedAmount;
        this.currency = currency;
        this.termMonths = termMonths;
        this.annualInterestRate = annualInterestRate;
        this.monthlyPayment = monthlyPayment;
        this.totalToPay = totalToPay;
        this.amountUsd = amountUsd;
        this.amountEur = amountEur;
        this.exchangeRateDate = exchangeRateDate;
        this.status = status;
        this.statusReason = statusReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CreditApplication(String customerName, String customerEmail, String customerRfc,
                             BigDecimal requestedAmount, String currency, int termMonths,
                             BigDecimal annualInterestRate) {
        this.id = UUID.randomUUID();;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerRfc = customerRfc;
        this.requestedAmount = requestedAmount;
        this.currency = currency;
        this.termMonths = termMonths;
        this.annualInterestRate = annualInterestRate;
        this.monthlyPayment = BigDecimal.valueOf(2);
        this.totalToPay = BigDecimal.valueOf(2);
        this.status = CreditStatus.CREATED;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void setExchangeRates(BigDecimal rateUsd, BigDecimal rateEur, LocalDate rateDate) {
        setAmounts(rateUsd, rateEur);
        this.exchangeRateDate = rateDate;
        this.updatedAt = OffsetDateTime.now();
    }

    private void setAmounts(BigDecimal rateUsd, BigDecimal rateEur){
        if (rateUsd != null){
            this.amountUsd = requestedAmount.multiply(rateUsd).setScale(2, RoundingMode.HALF_UP);
        }
        if (rateEur != null){
            this.amountEur = requestedAmount.multiply(rateEur).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
