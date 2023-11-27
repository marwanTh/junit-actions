package com.pixelogicmedia.delivery.api;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public abstract class BaseController {

    public static final String TOTAL_COUNT_HEADER = "X-Total-Count";

    protected Pageable pageOf(final Integer offset, final Integer limit) {
        final var size = limit == null ? 20 : limit;
        final var page = offset == null ? 0 : offset / size;
        return PageRequest.of(page, size);
    }

    protected <T> Specification<T> specificationOf(final String filter, final String sort) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(sort)) {
            return RSQLJPASupport.<T>toSpecification(filter).and(RSQLJPASupport.toSort(sort));
        }

        if (StringUtils.hasText(filter)) {
            return RSQLJPASupport.<T>toSpecification(filter);
        }

        if (StringUtils.hasText(sort)) {
            return RSQLJPASupport.toSort(sort);
        }

        return Specification.where((root, query, cb) -> cb.conjunction());
    }
}
