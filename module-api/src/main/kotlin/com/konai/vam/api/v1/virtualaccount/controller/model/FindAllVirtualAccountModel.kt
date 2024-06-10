package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import com.konai.vam.core.common.model.PageableRequest
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindAllVirtualAccountRequest(
    val accountNumber: String? = null,
    val bankCode: String? = null,
    val mappingType: String? = null,
    val isMapping: Boolean? = null,
    @field:NotNull
    val pageable: PageableRequest
)

data class FindAllVirtualAccountResponse(
    val pageable: BasePageable.Pageable,
    val content: List<VirtualAccountModel>
) : BaseResponse<FindAllVirtualAccountResponse>() {

    constructor(pageable: BasePageable<VirtualAccount>, mapper: (VirtualAccount) -> VirtualAccountModel): this(
        pageable = pageable.pageable,
        content = pageable.content.map(mapper)
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<FindAllVirtualAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }
}