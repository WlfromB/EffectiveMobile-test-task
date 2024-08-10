package org.example.effectivemobiletesttask.pagination;

import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface PageableCreator {
    Pageable create(PaginationParams paginationParams);
}
