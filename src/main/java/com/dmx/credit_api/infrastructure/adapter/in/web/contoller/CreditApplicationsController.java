package com.dmx.credit_api.infrastructure.adapter.in.web.contoller;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.port.in.CreateCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationUseCase;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListQuery;
import com.dmx.credit_api.domain.port.in.GetCreditApplicationsListUseCase;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreateCreditApplicationRequest;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreditApplicationResponse;
import com.dmx.credit_api.infrastructure.adapter.in.web.mapper.CreditApplicationMapper;
import com.dmx.credit_api.infrastructure.config.Constants;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(Constants.CREDIT_APPLICATIONS_BASE_PATH)
public class CreditApplicationsController {

    private final CreateCreditApplicationUseCase createCreditApplicationUseCase;
    private final GetCreditApplicationUseCase getCreditApplicationUseCase;
    private final GetCreditApplicationsListUseCase getCreditApplicationsListUseCase;
    private final CreditApplicationMapper creditApplicationMapper;

    public CreditApplicationsController(CreateCreditApplicationUseCase createCreditApplicationUseCase, GetCreditApplicationUseCase getCreditApplicationUseCase, GetCreditApplicationsListUseCase getCreditApplicationsListUseCase, CreditApplicationMapper creditApplicationMapper) {
        this.createCreditApplicationUseCase = createCreditApplicationUseCase;
        this.getCreditApplicationUseCase = getCreditApplicationUseCase;
        this.getCreditApplicationsListUseCase = getCreditApplicationsListUseCase;
        this.creditApplicationMapper = creditApplicationMapper;
    }

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> createApplication(@Valid @RequestBody CreateCreditApplicationRequest request){
        CreditApplication createdApplication = createCreditApplicationUseCase.execute(creditApplicationMapper.toCommand(request));
        CreditApplicationResponse response = creditApplicationMapper.toResponse(createdApplication);

        URI location = URI.create(Constants.CREDIT_APPLICATIONS_BASE_PATH);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationResponse> getApplicationById(@PathVariable UUID id){
        CreditApplication application = getCreditApplicationUseCase.execute(id);
        return ResponseEntity.ok(creditApplicationMapper.toResponse(application));
    }

    @GetMapping
    public ResponseEntity<Page<CreditApplicationResponse>> getApplicationsList(
            @RequestParam(required = false) CreditStatus status,
            @RequestParam(required = false) String customerRfc,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    ){
        PageRequest pageable = PageRequest.of(
                page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        GetCreditApplicationsListQuery query =
                new GetCreditApplicationsListQuery(status, customerRfc, minAmount, maxAmount);

        Page<CreditApplicationResponse> result =
                getCreditApplicationsListUseCase.execute(query, pageable).map(creditApplicationMapper::toResponse);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CreditApplicationResponse> changeStatus(@PathVariable String id){
        URI location = URI.create("/api/v1/credit-applications/");
        return ResponseEntity.created(location).body(null);
    }
}
