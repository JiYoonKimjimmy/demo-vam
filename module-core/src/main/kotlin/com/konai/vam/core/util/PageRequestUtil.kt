package com.konai.vam.core.util

import com.konai.vam.core.common.DEFAULT_SORT_BY
import com.konai.vam.core.common.DEFAULT_SORT_ORDER
import com.konai.vam.core.common.model.PageableRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

object PageRequestUtil {

    fun PageableRequest.toPageRequest(): PageRequest {
        return PageRequest.of(
            this.number,
            this.size,
            Sort.Direction.valueOf(this.sortOrder ?: DEFAULT_SORT_ORDER),
            *this.sortBy?.split(",")?.toTypedArray() ?: arrayOf(DEFAULT_SORT_BY)
        )
    }

}

