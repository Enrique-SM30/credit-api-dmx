package com.dmx.credit_api.infrastructure.adapter.out.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record FrankfurterResponse(
        @JsonProperty("amount")
        BigDecimal amount,
        @JsonProperty("base")
        String base,
        @JsonProperty("date")
        LocalDate date,
        @JsonProperty("rates")
        Map<String, BigDecimal> rates
) {}
