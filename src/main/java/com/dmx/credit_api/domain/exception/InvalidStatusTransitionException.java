package com.dmx.credit_api.domain.exception;

import com.dmx.credit_api.domain.model.CreditStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(CreditStatus from, CreditStatus to) {
        super(String.format(
                "Invalid Status: the %s status can't transition to %s", from, to
        ));
    }
}
