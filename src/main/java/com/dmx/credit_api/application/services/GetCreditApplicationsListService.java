package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListQuery;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetCreditApplicationsListService implements GetCreditApplicationsListUseCase {
    private  final CreditApplicationRepository repository;

    public GetCreditApplicationsListService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<CreditApplication> execute(GetCreditApplicationsListQuery query, Pageable pageable) {
        return repository.findAll(
                query.status(),
                query.customerRfc(),
                query.minAmount(),
                query.maxAmount(),
                pageable
        );
    }
}
