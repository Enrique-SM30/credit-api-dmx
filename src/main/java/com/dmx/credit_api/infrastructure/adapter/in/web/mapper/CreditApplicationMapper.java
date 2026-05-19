package com.dmx.credit_api.infrastructure.adapter.in.web.mapper;


import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationCommand;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreateCreditApplicationRequest;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreditApplicationResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationMapper {
    public CreateCreditApplicationCommand toCommand(CreateCreditApplicationRequest request) {
        return new CreateCreditApplicationCommand(
                request.customerName(),
                request.customerEmail(),
                request.customerRfc(),
                request.requestedAmount(),
                request.currency(),
                request.termMonths(),
                request.annualInterestRate()
        );
    }

    public CreditApplicationResponse toResponse(CreditApplication domain) {
        return new CreditApplicationResponse(
                domain.getId(),
                domain.getCustomerName(),
                domain.getCustomerEmail(),
                domain.getCustomerRfc(),
                domain.getRequestedAmount(),
                domain.getCurrency(),
                domain.getTermMonths(),
                domain.getAnnualInterestRate(),
                domain.getMonthlyPayment(),
                domain.getTotalToPay(),
                domain.getAmountUsd(),
                domain.getAmountEur(),
                domain.getExchangeRateDate(),
                domain.getStatus(),
                domain.getStatusReason(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
