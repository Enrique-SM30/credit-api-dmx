package com.dmx.credit_api.infrastructure.adapter.in.web.contoller;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationUseCase;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreateCreditApplicationRequest;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreditApplicationResponse;
import com.dmx.credit_api.infrastructure.adapter.in.web.mapper.CreditApplicationMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/credit-applications")
public class CreditApplicationsController {

    private final CreateCreditApplicationUseCase createCreditApplicationUseCase;
    private final CreditApplicationMapper creditApplicationMapper;

    public CreditApplicationsController(CreateCreditApplicationUseCase createCreditApplicationUseCase, CreditApplicationMapper creditApplicationMapper) {
        this.createCreditApplicationUseCase = createCreditApplicationUseCase;
        this.creditApplicationMapper = creditApplicationMapper;
    }

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> createApplication(@Valid @RequestBody CreateCreditApplicationRequest request){
        CreditApplication createdApplication = createCreditApplicationUseCase.execute(creditApplicationMapper.toCommand(request));
        CreditApplicationResponse response = creditApplicationMapper.toResponse(createdApplication);

        URI location = URI.create("/api/v1/credit-applications/");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationResponse> getApplicationById(@PathVariable String id){
        URI location = URI.create("/api/v1/credit-applications/");
        return ResponseEntity.created(location).body(null);
    }

    @GetMapping
    public ResponseEntity<CreditApplicationResponse> getAllApplications(){
        URI location = URI.create("/api/v1/credit-applications/");
        return ResponseEntity.created(location).body(null);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CreditApplicationResponse> changeStatus(@PathVariable String id){
        URI location = URI.create("/api/v1/credit-applications/");
        return ResponseEntity.created(location).body(null);
    }
}
