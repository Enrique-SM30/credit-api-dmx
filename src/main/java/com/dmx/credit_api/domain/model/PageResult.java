package com.dmx.credit_api.domain.model;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
