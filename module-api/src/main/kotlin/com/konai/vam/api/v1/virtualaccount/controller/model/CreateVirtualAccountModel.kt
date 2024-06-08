package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CreateVirtualAccountRequest(
    @field:NotBlank(message = "Account number must not be empty")
    val accountNumber: String,
    @field:Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits")
    val bankCode: String,
    @field:Size(min = 1, max = 20, message = "Bank name must be between 1 and 20 characters")
    val bankName: String,
)

data class CreateVirtualAccountResponse(
    val data: VirtualAccountModel
): BaseResponse<CreateVirtualAccountResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<CreateVirtualAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }
}