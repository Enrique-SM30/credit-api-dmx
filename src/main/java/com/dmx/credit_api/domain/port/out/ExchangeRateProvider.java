package com.dmx.credit_api.domain.port.out;

import java.util.Optional;

public interface ExchangeRateProvider {
    Optional<ExchangeRateResult> getRates(String baseCurrency, String... targetCurrencies);
}
