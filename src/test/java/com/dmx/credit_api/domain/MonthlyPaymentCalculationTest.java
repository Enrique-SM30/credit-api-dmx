package com.dmx.credit_api.domain;

import com.dmx.credit_api.domain.model.CreditApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cálculo de cuota mensual (amortización francesa)")
public class MonthlyPaymentCalculationTest {

    @Test
    @DisplayName("Caso base: 250,000 MXN a 18% anual a 24 meses")
    void shouldCalculateMonthlyPaymentForStandardCase() {
        BigDecimal principal = new BigDecimal("250000.00");
        BigDecimal annualRate = new BigDecimal("0.18");
        int termMonths = 24;

        BigDecimal payment = CreditApplication.calculateMonthlyPayment(principal, annualRate, termMonths);

        assertThat(payment).isBetween(
                new BigDecimal("12400.00"),
                new BigDecimal("12600.00")
        );
    }

    @Test
    @DisplayName("Tasa cero: cuota = principal / plazo (sin interés)")
    void shouldCalculateSimpleDivisionWhenRateIsZero() {
        // Arrange
        BigDecimal principal = new BigDecimal("120000.00");
        BigDecimal annualRate = BigDecimal.ZERO;
        int termMonths = 12;

        // Act
        BigDecimal payment = CreditApplication.calculateMonthlyPayment(principal, annualRate, termMonths);

        // Assert
        assertThat(payment).isEqualByComparingTo(new BigDecimal("10000.00"));
    }

    @Test
    @DisplayName("Tasa máxima (60%) y plazo máximo (60 meses)")
    void shouldHandleMaxRateAndTerm() {
        BigDecimal principal = new BigDecimal("1000000.00");
        BigDecimal annualRate = new BigDecimal("0.60");
        int termMonths = 60;

        BigDecimal payment = CreditApplication.calculateMonthlyPayment(principal, annualRate, termMonths);

        // Con 60% anual el pago mensual es alto — debe ser mayor que principal/term
        BigDecimal simpleDivision = new BigDecimal("1000000.00").divide(new BigDecimal("60"), 2, java.math.RoundingMode.HALF_UP);
        assertThat(payment).isGreaterThan(simpleDivision);
    }

    @ParameterizedTest(name = "P={0}, rate={1}, n={2} → pago esperado aprox {3}")
    @CsvSource({
            "100000.00, 0.12, 12,  8884.88",
            "500000.00, 0.24, 36, 19635.72",
            "50000.00,  0.18, 6,   8813.56"
    })
    @DisplayName("Múltiples escenarios parametrizados")
    void shouldCalculateCorrectlyForMultipleScenarios(
            BigDecimal principal,
            BigDecimal annualRate,
            int termMonths,
            BigDecimal expectedApprox
    ) {
        BigDecimal payment = CreditApplication.calculateMonthlyPayment(principal, annualRate, termMonths);

        // Tolerancia de ±50 MXN para cubrir redondeos
        assertThat(payment).isBetween(
                expectedApprox.subtract(new BigDecimal("50")),
                expectedApprox.add(new BigDecimal("50"))
        );
    }

}
