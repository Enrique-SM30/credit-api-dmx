package com.dmx.credit_api.infrastructure.adapter.in.web.contoller;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.domain.model.PageRequest;
import com.dmx.credit_api.domain.model.PageResult;
import com.dmx.credit_api.domain.port.in.*;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.ChangeStatusRequest;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreateCreditApplicationRequest;
import com.dmx.credit_api.infrastructure.adapter.in.web.dto.CreditApplicationResponse;
import com.dmx.credit_api.infrastructure.adapter.in.web.mapper.CreditApplicationMapper;
import com.dmx.credit_api.infrastructure.config.Constants;
import jakarta.validation.Valid;
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
    private final ChangeStatusUseCase changeStatusUseCase;
    private final CreditApplicationMapper creditApplicationMapper;

    public CreditApplicationsController(CreateCreditApplicationUseCase createCreditApplicationUseCase, GetCreditApplicationUseCase getCreditApplicationUseCase, GetCreditApplicationsListUseCase getCreditApplicationsListUseCase, ChangeStatusUseCase changeStatusUseCase, CreditApplicationMapper creditApplicationMapper) {
        this.createCreditApplicationUseCase = createCreditApplicationUseCase;
        this.getCreditApplicationUseCase = getCreditApplicationUseCase;
        this.getCreditApplicationsListUseCase = getCreditApplicationsListUseCase;
        this.changeStatusUseCase = changeStatusUseCase;
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
    public ResponseEntity<PageResult<CreditApplicationResponse>> getApplicationsList(
            @RequestParam(required = false) CreditStatus status,
            @RequestParam(required = false) String customerRfc,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    ){
        PageRequest pageRequest = new PageRequest(page, pageSize);

        GetCreditApplicationsListQuery query =
                new GetCreditApplicationsListQuery(status, customerRfc, minAmount, maxAmount);

        PageResult<CreditApplication> result =
                getCreditApplicationsListUseCase.execute(query, pageRequest);

        PageResult<CreditApplicationResponse> response = new PageResult<>(
                result.content().stream().map(creditApplicationMapper::toResponse).toList(),
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CreditApplicationResponse> changeStatus(@PathVariable UUID id, @Valid @RequestBody ChangeStatusRequest request){
        CreditApplication updated = changeStatusUseCase.execute(id, request.status(), request.reason());
        return ResponseEntity.ok(creditApplicationMapper.toResponse(updated));
    }
}
