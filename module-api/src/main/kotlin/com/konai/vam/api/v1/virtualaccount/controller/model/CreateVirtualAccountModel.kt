package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.common.model.BaseResponse
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CreateVirtualAccountRequest(
    @field:NotBlank(message = "Account number must not be empty")
    @field:Length(min = 1, max = 20, message = "Account number lengths are allowed from 1 to 20 characters.")
    val accountNo: String,
    @field:NotBlank(message = "Bank code must not be empty")
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits")
    val bankCode: String,
    val connectType: VirtualAccountConnectType,
    val parentAccountId: Long
)

data class CreateVirtualAccountResponse(
    val data: VirtualAccountModel
): BaseResponse<CreateVirtualAccountResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<CreateVirtualAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }
}