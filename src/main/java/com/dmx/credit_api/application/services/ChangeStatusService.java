package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.exception.CreditApplicationNotFoundException;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.port.in.ChangeStatusUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeStatusService implements ChangeStatusUseCase {
    private final CreditApplicationRepository repository;


    public ChangeStatusService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreditApplication execute(UUID id, CreditStatus newStatus, String reason) {
        CreditApplication application = repository.findById(id).orElseThrow(() -> new CreditApplicationNotFoundException(id));

        application.changeStatus(newStatus, reason);

        return repository.save(application);
    }
}
