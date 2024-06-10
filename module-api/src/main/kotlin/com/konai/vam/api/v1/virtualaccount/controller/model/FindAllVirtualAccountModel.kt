package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccounts
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import com.konai.vam.core.common.model.PageableRequest
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class FindAllVirtualAccount {

    data class Request(
        @field:NotNull
        val pageRequest: PageableRequest
    )

    data class Response(
        val pageable: BasePageable.Pageable,
        val content: List<VirtualAccountModel>
    ) : BaseResponse<Response>() {

        constructor(virtualAccounts: VirtualAccounts, converter: (VirtualAccount) -> VirtualAccountModel): this(
            pageable = virtualAccounts.pageable,
            content = virtualAccounts.content.map(converter)
        )

        override fun success(httpStatus: HttpStatus): ResponseEntity<Response> {
            return ResponseEntity(this, httpStatus)
        }
    }

}