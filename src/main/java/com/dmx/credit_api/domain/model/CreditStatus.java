package com.dmx.credit_api.domain.model;

import java.util.Map;
import java.util.Set;

public enum CreditStatus {
    CREATED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    CANCELLED;

    //Lógica de transicion de status
    private static final Map<CreditStatus, Set<CreditStatus>> VALID_TRANSITIONS = Map.of(
            CREATED, Set.of(UNDER_REVIEW, CANCELLED),
            UNDER_REVIEW, Set.of(APPROVED, REJECTED, CANCELLED),
            APPROVED, Set.of(),
            REJECTED, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean canTransitionTo(CreditStatus newStatus) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(newStatus);
    }
}
