package com.konai.vam.api.v1.virtualaccountbank.controller.model

import com.konai.vam.api.v1.virtualaccount.controller.model.FindAllVirtualAccountResponse
import com.konai.vam.api.v1.virtualaccount.controller.model.VirtualAccountModel
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBank
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindAllVirtualAccountBankRequest(
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits")
    val bankCode: String? = null,
    @field:Length(min = 1, max = 20, message = "bankName lengths are allowed from 1 to 20 characters.")
    val bankName: String? = null
)

data class FindAllVirtualAccountBankResponse(
    val pageable: BasePageable.Pageable,
    val content: List<VirtualAccountBankModel>
) : BaseResponse<FindAllVirtualAccountBankResponse>() {

    constructor(pageable: BasePageable<VirtualAccountBank>, mapper: (VirtualAccountBank) -> VirtualAccountBankModel): this(
        pageable = pageable.pageable,
        content = pageable.content.map(mapper)
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<FindAllVirtualAccountBankResponse> {
        return ResponseEntity(this, httpStatus)
    }
}