package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GetCreditApplicationsListUseCase {
    Page<CreditApplication> execute(GetCreditApplicationsListQuery query, Pageable pageable);
}
