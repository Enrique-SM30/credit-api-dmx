package com.dmx.credit_api.domain.port.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record ExchangeRateResult(
        Map<String, BigDecimal> rates,
        LocalDate date
){
    public BigDecimal getRate(String currency) { return rates.get(currency); }
}
