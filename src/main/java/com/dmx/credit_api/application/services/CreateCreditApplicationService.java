package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationCommand;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import com.dmx.credit_api.domain.port.out.ExchangeRateProvider;
import com.dmx.credit_api.domain.port.out.ExchangeRateResult;
import com.dmx.credit_api.infrastructure.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CreateCreditApplicationService implements CreateCreditApplicationUseCase {

    private final CreditApplicationRepository repository;
    private final ExchangeRateProvider exchangeRateProvider;

    public CreateCreditApplicationService(CreditApplicationRepository repository, ExchangeRateProvider exchangeRateProvider) {
        this.repository = repository;
        this.exchangeRateProvider = exchangeRateProvider;
    }

    @Override
    public CreditApplication execute(CreateCreditApplicationCommand command) {
        CreditApplication application = new CreditApplication(
                command.customerName(),
                command.customerEmail(),
                command.customerRfc(),
                command.requestedAmount(),
                command.currency(),
                command.termMonths(),
                command.annualInterestRate()
        );

        try{
            Optional<ExchangeRateResult> ratesOptions = exchangeRateProvider.getRates(Constants.CURRENCY_MXN, Constants.CURRENCY_USD, Constants.CURRENCY_EUR);

            ratesOptions.ifPresentOrElse(
                    result -> {
                        application.setExchangeRates(result.getRate(Constants.CURRENCY_USD), result.getRate(Constants.CURRENCY_EUR), result.date());

                        log.info("Tipos de cambio obtenidos correctamente para solicitud {}",
                                application.getId());
                    },
                    () -> log.warn("No se obtuvieron tipos de cambio para solicitud {}. Los campos amountUsd/amountEur quedarán en null.",
                            application.getId())
            );
        } catch (Exception ex) {
            log.warn("Error in get of exchange types of request: {}", application.getId());
        }

        return repository.save(application);
    }
}
