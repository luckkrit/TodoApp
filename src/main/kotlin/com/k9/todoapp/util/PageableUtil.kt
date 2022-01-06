package com.k9.todoapp.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.*

object PageableUtil {
    fun getPageable(
        optionalPage: Optional<Int>,
        optionalLimit: Optional<Int>,
        optionalSort: Optional<Sort.Direction>,
        optionalTotal: Optional<Int>,
        optionalSortBy: Optional<String>
    ): Pageable {
        val offset = if (optionalPage.isPresent) optionalPage.get() else 0
        val total = if (optionalTotal.isPresent) optionalTotal.get() else 5
        val limit = if (optionalLimit.isPresent) optionalLimit.get() else total
        val direction = if (optionalSort.isPresent) optionalSort.get() else Sort.Direction.ASC
        val sortBy = if (optionalSortBy.isPresent) optionalSortBy.get() else "id"
        return PageRequest.of(offset, limit, direction, sortBy)
    }
}