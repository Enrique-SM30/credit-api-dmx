package com.dmx.credit_api.domain.port.out;

import com.dmx.credit_api.domain.model.CreditApplicationEvent;

public interface CreditApplicationEventRepository {
    void save(CreditApplicationEvent event);
}
