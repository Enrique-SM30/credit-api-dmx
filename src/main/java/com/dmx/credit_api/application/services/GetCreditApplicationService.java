package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.exception.CreditApplicationNotFoundException;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetCreditApplicationService implements GetCreditApplicationUseCase {
    private final CreditApplicationRepository repository;

    public GetCreditApplicationService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreditApplication execute(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CreditApplicationNotFoundException(id));
    }
}
