package com.dmx.credit_api.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateCreditApplicationRequest(

        @NotBlank(message = "customerName is a must")
        @Size(min = 3, max = 120, message = "curtomerName length must be between 3 and 120")
        String customerName,

        @NotBlank(message = "customerEmail is a must")
        @Email(message = "customerEmail must have a correct email format")
        String customerEmail,

        @Pattern(regexp = "^[A-Za-z0-9]{12,13}$", message = "customerRfc length must be between 12 and 13, and can only contain alphanumerics characters")
        String customerRfc,

        @NotNull(message = "requestedAmount is a must")
        @DecimalMin(value = "0.01", message = "requestedAmount must be greater than 0")
        @DecimalMax(value = "5000000.00", message = "requestedAmount can't be greater than 5,000,000")
        BigDecimal requestedAmount,

        @NotBlank(message = "currency is a must")
        @Pattern(regexp = "^MXN$", message = "currency only accept MXN at the moment")
        String currency,

        @NotNull(message = "termMonths is a must")
        @Min(value = 6, message = "termMonths must be at least 6")
        @Max(value = 60, message = "termMonths can't be greater than 60")
        Integer termMonths,

        @NotNull(message = "annualInterestRate is a must")
        @DecimalMin(value = "0.05", message = "annualInterestRate must be at least 0.05 (5%)")
        @DecimalMax(value = "0.60", message = "annualInterestRate can't be greater than 0.60 (60%)")
        BigDecimal annualInterestRate
) {}
