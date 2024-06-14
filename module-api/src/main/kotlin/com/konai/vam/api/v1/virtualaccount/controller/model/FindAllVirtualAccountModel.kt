package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindAllVirtualAccountRequest(
    @field:Length(min = 1, max = 20, message = "Account number lengths are allowed from 1 to 20 characters.")
    val accountNumber: String? = null,
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits")
    val bankCode: String? = null,
    val mappingType: VirtualAccountConnectType? = null,
    val isMapping: Boolean? = null,
    @field:NotNull(message = "Pageable request cannot be empty")
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