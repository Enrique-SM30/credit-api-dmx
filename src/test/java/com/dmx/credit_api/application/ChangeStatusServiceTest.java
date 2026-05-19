package com.dmx.credit_api.application;

import com.dmx.credit_api.application.services.ChangeStatusService;
import com.dmx.credit_api.domain.exception.InvalidStatusTransitionException;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChangeStatusService")
public class ChangeStatusServiceTest {
    @Mock
    private CreditApplicationRepository repository;

    private ChangeStatusService service;

    @BeforeEach
    void setUp() {
        service = new ChangeStatusService(repository);
    }

    private CreditApplication createApplication() {
        return new CreditApplication(
                "Test User", "test@test.com", null,
                new BigDecimal("100000.00"), "MXN", 12, new BigDecimal("0.18")
        );
    }

    @Test
    @DisplayName("Transición válida CREATED a UNDER_REVIEW cambia al nuevo estado")
    void shouldChangeStatusSuccessfully() {
        // Arrange
        CreditApplication app = createApplication();
        when(repository.findById(app.getId())).thenReturn(Optional.of(app));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        CreditApplication result = service.execute(app.getId(), CreditStatus.UNDER_REVIEW, "En revisión");

        // Assert
        assertThat(result.getStatus()).isEqualTo(CreditStatus.UNDER_REVIEW);
        assertThat(result.getStatusReason()).isEqualTo("En revisión");
        verify(repository).save(app);
    }

    @Test
    @DisplayName("Transición inválida CREATED a APPROVED lanza InvalidStatusTransitionException")
    void shouldThrowInvalidTransitionException() {
        CreditApplication app = createApplication();
        when(repository.findById(app.getId())).thenReturn(Optional.of(app));

        assertThatThrownBy(() -> service.execute(app.getId(), CreditStatus.APPROVED, null))
                .isInstanceOf(InvalidStatusTransitionException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Estado terminal APPROVED no permite más transiciones")
    void shouldNotAllowTransitionFromTerminalState() {
        CreditApplication app = createApplication();
        app.changeStatus(CreditStatus.UNDER_REVIEW, null);
        app.changeStatus(CreditStatus.APPROVED, null);

        when(repository.findById(app.getId())).thenReturn(Optional.of(app));

        assertThatThrownBy(() -> service.execute(app.getId(), CreditStatus.CANCELLED, null))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }
}
