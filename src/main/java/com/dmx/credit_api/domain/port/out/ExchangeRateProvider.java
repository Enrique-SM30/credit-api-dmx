package com.dmx.credit_api.domain.port.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface ExchangeRateProvider {
    Optional<ExchangeRateResult> getRates(String baseCurrency, String... targetCurrencies);
}
