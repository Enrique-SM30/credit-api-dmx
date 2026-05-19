package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.PageRequest;
import com.dmx.credit_api.domain.model.PageResult;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListQuery;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetCreditApplicationsListService implements GetCreditApplicationsListUseCase {
    private  final CreditApplicationRepository repository;

    public GetCreditApplicationsListService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<CreditApplication> execute(GetCreditApplicationsListQuery query, PageRequest pageRequest) {
        return repository.findAll(
                query.status(),
                query.customerRfc(),
                query.minAmount(),
                query.maxAmount(),
                pageRequest
        );
    }
}
