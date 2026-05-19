package com.dmx.credit_api.application;

import com.dmx.credit_api.application.services.CreateCreditApplicationService;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationCommand;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import com.dmx.credit_api.domain.port.out.ExchangeRateProvider;
import com.dmx.credit_api.domain.port.out.ExchangeRateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCreditApplicationService")
public class CreateCreditApplicationServiceTest {
    @Mock
    private CreditApplicationRepository repository;

    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    private CreateCreditApplicationService service;

    private CreateCreditApplicationCommand validCommand;

    @BeforeEach
    void setUp() {
        service = new CreateCreditApplicationService(repository, exchangeRateProvider);

        validCommand = new CreateCreditApplicationCommand(
                "María García",
                "maria@test.com",
                "GARM850101ABC",
                new BigDecimal("250000.00"),
                "MXN",
                24,
                new BigDecimal("0.18")
        );

        when(repository.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Cuando frankfurter responde bien guarda usd y eur en la solicitud")
    void shouldEnrichWithExchangeRatesWhenProviderSucceeds() {
        // Arrange
        ExchangeRateResult rates = new ExchangeRateResult(
                Map.of("USD", new BigDecimal("0.058"), "EUR", new BigDecimal("0.052")),
                LocalDate.now()
        );
        when(exchangeRateProvider.getRates("MXN", "USD", "EUR"))
                .thenReturn(Optional.of(rates));

        // Act
        CreditApplication result = service.execute(validCommand);

        // Assert
        assertThat(result.getAmountUsd()).isNotNull();
        assertThat(result.getAmountEur()).isNotNull();
        assertThat(result.getAmountUsd()).isEqualByComparingTo(new BigDecimal("14500.00"));
        assertThat(result.getAmountEur()).isEqualByComparingTo(new BigDecimal("13000.00"));
    }

    @Test
    @DisplayName("Cuando Frankfurter retorna Optional.empty() la solicitud se crea con USD/EUR null")
    void shouldCreateApplicationWithNullRatesWhenProviderReturnsEmpty() {
        when(exchangeRateProvider.getRates("MXN", "USD", "EUR"))
                .thenReturn(Optional.empty());

        CreditApplication result = service.execute(validCommand);

        assertThat(result).isNotNull();
        assertThat(result.getAmountUsd()).isNull();
        assertThat(result.getAmountEur()).isNull();
        assertThat(result.getStatus()).isEqualTo(CreditStatus.CREATED);
    }

    @Test
    @DisplayName("Cuando Frankfurter lanza excepción la solicitud se crea con USD/EUR null (tolerancia a fallos)")
    void shouldCreateApplicationWhenProviderThrowsException() {
        when(exchangeRateProvider.getRates("MXN", "USD", "EUR"))
                .thenThrow(new RuntimeException("Connection timeout"));

        CreditApplication result = service.execute(validCommand);

        assertThat(result).isNotNull();
        assertThat(result.getAmountUsd()).isNull();
        assertThat(result.getAmountEur()).isNull();
    }


    @Test
    @DisplayName("La cuota mensual se calcula correctamente al ejecutar el comando")
    void shouldCalculateMonthlyPaymentCorrectly() {
        when(exchangeRateProvider.getRates(any(), any()))
                .thenReturn(Optional.empty());

        CreditApplication result = service.execute(validCommand);

        // monthlyPayment debe ser positivo y menor que el monto total
        assertThat(result.getMonthlyPayment()).isGreaterThan(BigDecimal.ZERO);
        assertThat(result.getTotalToPay()).isGreaterThan(result.getRequestedAmount());
    }
}
