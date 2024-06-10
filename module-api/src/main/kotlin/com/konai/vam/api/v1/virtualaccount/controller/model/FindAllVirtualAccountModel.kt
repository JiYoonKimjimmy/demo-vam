package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import com.konai.vam.core.common.model.PageableRequest
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class FindAllVirtualAccount {

    data class Request(
        @field:NotNull
        val pageable: PageableRequest
    )

    data class Response(
        val pageable: BasePageable.Pageable,
        val content: List<VirtualAccountModel>
    ) : BaseResponse<Response>() {

        constructor(pageable: BasePageable<VirtualAccount>, mapper: (VirtualAccount) -> VirtualAccountModel): this(
            pageable = pageable.pageable,
            content = pageable.content.map(mapper)
        )

        override fun success(httpStatus: HttpStatus): ResponseEntity<Response> {
            return ResponseEntity(this, httpStatus)
        }
    }

}