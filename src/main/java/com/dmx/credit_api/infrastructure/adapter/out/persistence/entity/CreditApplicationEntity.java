package com.dmx.credit_api.infrastructure.adapter.out.persistence.entity;

import com.dmx.credit_api.domain.model.CreditStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_applications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 160)
    private String customerEmail;

    @Column(name = "customer_rfc", length = 13)
    private String customerRfc;

    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(name = "currency", nullable = false, columnDefinition = "CHAR(3)")
    private String currency;

    @Column(name = "term_months", nullable = false)
    private int termMonths;

    @Column(name = "annual_interest_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal annualInterestRate;

    @Column(name = "monthly_payment", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(name = "total_to_pay", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalToPay;

    @Column(name = "amount_usd", precision = 15, scale = 2)
    private BigDecimal amountUsd;

    @Column(name = "amount_eur", precision = 15, scale = 2)
    private BigDecimal amountEur;

    @Column(name = "exchange_rate_date")
    private LocalDate exchangeRateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CreditStatus status;

    @Column(name = "status_reason", columnDefinition = "TEXT")
    private String statusReason;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime updatedAt;
}
