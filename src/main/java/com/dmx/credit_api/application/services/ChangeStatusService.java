package com.dmx.credit_api.application.services;

import com.dmx.credit_api.domain.exception.CreditApplicationNotFoundException;
import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditApplicationEvent;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.port.in.ChangeStatusUseCase;
import com.dmx.credit_api.domain.port.out.CreditApplicationEventRepository;
import com.dmx.credit_api.domain.port.out.CreditApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeStatusService implements ChangeStatusUseCase {
    private final CreditApplicationRepository repository;
    private final CreditApplicationEventRepository eventRepository;

    public ChangeStatusService(CreditApplicationRepository repository, CreditApplicationEventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CreditApplication execute(UUID id, CreditStatus newStatus, String reason) {
        CreditApplication application = repository.findById(id).orElseThrow(() -> new CreditApplicationNotFoundException(id));

        CreditStatus oldStatus = application.getStatus();

        application.changeStatus(newStatus, reason);
        CreditApplication saved = repository.save(application);

        eventRepository.save(new CreditApplicationEvent(
                application.getId(),
                "STATUS_CHANGED",
                oldStatus,
                newStatus,
                "system",
                "{\"reason\": \"" + reason + "\"}"
        ));

        return saved;
    }
}
