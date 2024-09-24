package com.konai.vam.api.v1.parentaccount.controller.model

import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class UpdateParentAccountRequest(
    @field:Length(min = 1, max = 20, message = "Parent account number lengths are allowed from 1 to 20 characters.")
    val parentAccountNo: String? = null,
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits.")
    val bankCode: String? = null
)

data class UpdateParentAccountResponse(
    val data: ParentAccountModel
): BaseResponse<UpdateParentAccountResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<UpdateParentAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }
}