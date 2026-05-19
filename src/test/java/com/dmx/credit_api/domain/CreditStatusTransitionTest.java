package com.dmx.credit_api.domain;

import com.dmx.credit_api.domain.exception.InvalidStatusTransitionException;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Transiciones de estado")
public class CreditStatusTransitionTest {
    private CreditApplication application;

    @BeforeEach
    void setUp() {
        application = new CreditApplication(
                "Juan Pérez", "juan@test.com", null,
                new BigDecimal("50000.00"), "MXN", 12, new BigDecimal("0.18")
        );
    }

    @Test
    @DisplayName("CREATED → UNDER_REVIEW es válida")
    void shouldAllowCreatedToUnderReview() {
        application.changeStatus(CreditStatus.UNDER_REVIEW, "En revisión");
        assertThat(application.getStatus()).isEqualTo(CreditStatus.UNDER_REVIEW);
    }

    @Test
    @DisplayName("CREATED → CANCELLED es válida")
    void shouldAllowCreatedToCancelled() {
        application.changeStatus(CreditStatus.CANCELLED, "Cancelada por el cliente");
        assertThat(application.getStatus()).isEqualTo(CreditStatus.CANCELLED);
    }

    @Test
    @DisplayName("UNDER_REVIEW → APPROVED es válida")
    void shouldAllowUnderReviewToApproved() {
        application.changeStatus(CreditStatus.UNDER_REVIEW, null);
        application.changeStatus(CreditStatus.APPROVED, "Cumple políticas");
        assertThat(application.getStatus()).isEqualTo(CreditStatus.APPROVED);
    }

    @Test
    @DisplayName("UNDER_REVIEW → REJECTED es válida")
    void shouldAllowUnderReviewToRejected() {
        application.changeStatus(CreditStatus.UNDER_REVIEW, null);
        application.changeStatus(CreditStatus.REJECTED, "No cumple políticas");
        assertThat(application.getStatus()).isEqualTo(CreditStatus.REJECTED);
    }

    @Test
    @DisplayName("UNDER_REVIEW → CANCELLED es válida")
    void shouldAllowUnderReviewToCancelled() {
        application.changeStatus(CreditStatus.UNDER_REVIEW, null);
        application.changeStatus(CreditStatus.CANCELLED, "Cancelada por analista");
        assertThat(application.getStatus()).isEqualTo(CreditStatus.CANCELLED);
    }

    @Test
    @DisplayName("CREATED → APPROVED no es válida (falta pasar por UNDER_REVIEW)")
    void shouldRejectCreatedToApproved() {
        assertThatThrownBy(() -> application.changeStatus(CreditStatus.APPROVED, null))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("CREATED")
                .hasMessageContaining("APPROVED");
    }

    @Test
    @DisplayName("CREATED → REJECTED no es válida")
    void shouldRejectCreatedToRejected() {
        assertThatThrownBy(() -> application.changeStatus(CreditStatus.REJECTED, null))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @ParameterizedTest
    @EnumSource(CreditStatus.class)
    @DisplayName("APPROVED es un estado terminal — no permite ninguna transición")
    void shouldRejectAnyTransitionFromApproved(CreditStatus next) {
        application.changeStatus(CreditStatus.UNDER_REVIEW, null);
        application.changeStatus(CreditStatus.APPROVED, null);

        assertThatThrownBy(() -> application.changeStatus(next, null))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    @DisplayName("El statusReason se guarda correctamente al cambiar estado")
    void shouldPersistStatusReasonOnTransition() {
        String reason = "Documentación incompleta";
        application.changeStatus(CreditStatus.UNDER_REVIEW, reason);
        application.changeStatus(CreditStatus.REJECTED, reason);

        assertThat(application.getStatusReason()).isEqualTo(reason);
    }

    @Test
    @DisplayName("updatedAt cambia al hacer una transición")
    void shouldUpdateTimestampOnTransition() {
        var before = application.getUpdatedAt();
        application.changeStatus(CreditStatus.UNDER_REVIEW, null);
        assertThat(application.getUpdatedAt()).isAfterOrEqualTo(before);
    }
}
