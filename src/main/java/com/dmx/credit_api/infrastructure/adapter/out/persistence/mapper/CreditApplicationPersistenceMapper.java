package com.dmx.credit_api.infrastructure.adapter.out.persistence.mapper;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationPersistenceMapper {
    public CreditApplicationEntity toEntity(CreditApplication application) {
        return CreditApplicationEntity.builder()
                .id(application.getId())
                .customerName(application.getCustomerName())
                .customerEmail(application.getCustomerEmail())
                .customerRfc(application.getCustomerRfc())
                .requestedAmount(application.getRequestedAmount())
                .currency(application.getCurrency())
                .termMonths(application.getTermMonths())
                .annualInterestRate(application.getAnnualInterestRate())
                .monthlyPayment(application.getMonthlyPayment())
                .totalToPay(application.getTotalToPay())
                .amountUsd(application.getAmountUsd())
                .amountEur(application.getAmountEur())
                .exchangeRateDate(application.getExchangeRateDate())
                .status(application.getStatus())
                .statusReason(application.getStatusReason())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    public CreditApplication toDomainEntity(CreditApplicationEntity entity) {
        return new CreditApplication(
                entity.getId(),
                entity.getCustomerName(),
                entity.getCustomerEmail(),
                entity.getCustomerRfc(),
                entity.getRequestedAmount(),
                entity.getCurrency(),
                entity.getTermMonths(),
                entity.getAnnualInterestRate(),
                entity.getMonthlyPayment(),
                entity.getTotalToPay(),
                entity.getAmountUsd(),
                entity.getAmountEur(),
                entity.getExchangeRateDate(),
                entity.getStatus(),
                entity.getStatusReason(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
