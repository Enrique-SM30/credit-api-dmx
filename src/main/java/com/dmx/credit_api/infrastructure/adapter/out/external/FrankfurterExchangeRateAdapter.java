package com.dmx.credit_api.infrastructure.adapter.out.external;

import com.dmx.credit_api.domain.port.out.ExchangeRateProvider;
import com.dmx.credit_api.domain.port.out.ExchangeRateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Slf4j
@Component
public class FrankfurterExchangeRateAdapter implements ExchangeRateProvider {
    private final RestClient restClient;


    public FrankfurterExchangeRateAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<ExchangeRateResult> getRates(String baseCurrency, String... targetCurrencies) {
        String symbols = String.join(",", targetCurrencies);
        try {

            FrankfurterResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("base", baseCurrency)
                            .queryParam("symbols", symbols)
                            .build())
                    .retrieve()
                    .body(FrankfurterResponse.class);
            if(response == null || response.rates() == null){
                log.error("Frankfurter response is empty for base {}, symbols {}", baseCurrency, symbols);
                return Optional.empty();
            }

            return Optional.of(new ExchangeRateResult(response.rates(), response.date()));

        } catch (RestClientException ex) {
            log.error("Error in Frankfurter get base {}, symbols {}, error {}", baseCurrency, symbols, ex.getMessage());

            return Optional.empty();
        }
    }
}
