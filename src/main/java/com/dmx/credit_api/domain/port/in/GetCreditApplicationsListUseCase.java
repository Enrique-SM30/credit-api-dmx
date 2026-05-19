package com.dmx.credit_api.domain.port.in;

import com.dmx.credit_api.domain.model.CreditApplication;
import com.dmx.credit_api.domain.model.PageRequest;
import com.dmx.credit_api.domain.model.PageResult;


public interface GetCreditApplicationsListUseCase {
    PageResult<CreditApplication> execute(GetCreditApplicationsListQuery query, PageRequest pageRequest);
}
