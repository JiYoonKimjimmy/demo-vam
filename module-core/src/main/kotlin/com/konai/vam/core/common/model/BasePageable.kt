package com.konai.vam.core.common.model

import org.springframework.data.domain.Page

open class BasePageable<T>(
    open val pageable: Pageable = Pageable(),
    open val content: List<T> = emptyList(),
) {

    data class Pageable(
        val first: Boolean = false,
        val last: Boolean = false,
        val number: Int = 0,
        val numberOfElements: Int = 0,
        val size: Int = 10,
        val totalPages: Int = 0,
        val totalElements: Int = 0,
    )

    constructor(result: Page<T>) : this(
        pageable = Pageable(
            first = result.isFirst,
            last = result.isLast,
            number = result.number,
            numberOfElements = result.numberOfElements,
            size = result.size,
            totalPages = result.totalPages,
            totalElements = result.numberOfElements,
        ),
        content = result.content
    )
}