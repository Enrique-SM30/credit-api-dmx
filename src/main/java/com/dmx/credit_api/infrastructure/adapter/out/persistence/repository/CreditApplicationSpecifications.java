package com.dmx.credit_api.infrastructure.adapter.out.persistence.repository;

import com.dmx.credit_api.domain.model.CreditStatus;
import com.dmx.credit_api.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class CreditApplicationSpecifications {

    public static Specification<CreditApplicationEntity> hasStatus(CreditStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<CreditApplicationEntity> hasRfc(String rfc) {
        return (root, query, cb) ->
                (rfc == null || rfc.isBlank())
                    ? cb.conjunction() : cb.equal(root.get("customerRfc"), rfc);
    }

    public static Specification<CreditApplicationEntity> minAmount(BigDecimal min) {
        return (root, query, cb) ->
            min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("requestedAmount"), min);
    }

    public static Specification<CreditApplicationEntity> maxAmount(BigDecimal max) {
        return (root, query, cb) ->
                max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("requestedAmount"), max);
    }
}
