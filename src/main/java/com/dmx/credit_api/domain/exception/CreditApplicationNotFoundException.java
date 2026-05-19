package com.dmx.credit_api.domain.exception;

import java.util.UUID;

public class CreditApplicationNotFoundException extends RuntimeException {
    public CreditApplicationNotFoundException(UUID id) {
        super(String.format("Credit request with id %s not found", id));
    }
}
