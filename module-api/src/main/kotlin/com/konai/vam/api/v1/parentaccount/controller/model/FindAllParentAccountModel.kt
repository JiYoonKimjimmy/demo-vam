package com.konai.vam.api.v1.parentaccount.controller.model

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindAllParentAccountRequest(
    @field:Length(min = 1, max = 20, message = "Parent account number lengths are allowed from 1 to 20 characters.")
    val parentAccountNo: String?,
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits.")
    val bankCode: String?
)

data class FindAllParentAccountResponse(
    val pageable: BasePageable.Pageable,
    val content: List<ParentAccountModel>
) : BaseResponse<FindAllParentAccountResponse>() {

    constructor(pageable: BasePageable<ParentAccount>, mapper: (ParentAccount) -> ParentAccountModel): this(
        pageable = pageable.pageable,
        content = pageable.content.map(mapper)
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<FindAllParentAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }

}