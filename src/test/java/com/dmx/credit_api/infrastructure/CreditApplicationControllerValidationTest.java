package com.dmx.credit_api.infrastructure;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.ChangeStatusUseCase;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListUseCase;
import com.dmx.credit_api.infrastructure.adapter.in.web.contoller.CreditApplicationsController;
import com.dmx.credit_api.infrastructure.adapter.in.web.mapper.CreditApplicationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditApplicationsController.class)
@DisplayName("Validaciones de entrada - POST /api/v1/credit-applications")
public class CreditApplicationControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean  private CreateCreditApplicationUseCase createUseCase;
    @MockitoBean  private GetCreditApplicationUseCase getUseCase;
    @MockitoBean  private GetCreditApplicationsListUseCase listUseCase;
    @MockitoBean  private ChangeStatusUseCase changeStatusUseCase;
    @MockitoBean  private CreditApplicationMapper mapper;

    private Map<String, Object> validBody() {
        return Map.of(
                "customerName",       "Juan Pérez Ramírez",
                "customerEmail",      "juan@example.com",
                "requestedAmount",    250000.00,
                "currency",           "MXN",
                "termMonths",         24,
                "annualInterestRate", 0.18
        );
    }

    @Test
    @DisplayName("Request válido → 201 Created")
    void shouldReturn201ForValidRequest() throws Exception {
        CreditApplication mockApp = new CreditApplication(
                "Juan Pérez Ramírez", "juan@example.com", null,
                new BigDecimal("250000.00"), "MXN", 24, new BigDecimal("0.18")
        );
        when(createUseCase.execute(any())).thenReturn(mockApp);
        when(mapper.toCommand(any())).thenCallRealMethod();
        when(mapper.toResponse(any())).thenCallRealMethod();

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBody())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("customerName vacío → 400 VALIDATION_ERROR")
    void shouldRejectBlankCustomerName() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("customerName", "");

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("customerEmail inválido → 400 VALIDATION_ERROR")
    void shouldRejectInvalidEmail() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("customerEmail", "no-es-un-email");

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("requestedAmount = 0 → 400 VALIDATION_ERROR")
    void shouldRejectZeroAmount() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("requestedAmount", 0);

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("requestedAmount > 5,000,000 → 400 VALIDATION_ERROR")
    void shouldRejectAmountExceedingMax() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("requestedAmount", 5_000_001);

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("customerRfc con longitud inválida (10 chars) → 400 VALIDATION_ERROR")
    void shouldRejectInvalidRfc() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("customerRfc", "ABCD123456"); // 10 chars — inválido

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("Respuesta de error incluye todos los campos del formato estándar")
    void shouldReturnStandardErrorFormat() throws Exception {
        var body = new java.util.HashMap<>(validBody());
        body.put("customerName", "");

        mockMvc.perform(post("/api/v1/credit-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/credit-applications"));
    }
}
