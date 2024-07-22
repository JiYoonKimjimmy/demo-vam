package com.konai.vam.api.v1.wooribank.controller.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class WooriBankManagementModel(

    @JsonUnwrapped
    @get:Valid
    val common: WooriBankCommonModel,

    @field:NotBlank(message = "parentAccount must not be empty")
    @field:Length(min = 1, max = 14, message = "parentAccount length are allowed from 1 to 14 characters.")
    val parentAccount: String,
    @field:NotBlank(message = "trDate must not be empty")
    @field:Length(min = 8, max = 8, message = "trDate length are allowed from 8 to 8 characters.")
    val trDate: String,
    @field:NotBlank(message = "trTime must not be empty")
    @field:Length(min = 6, max = 6, message = "trTime length are allowed from 8 to 8 characters.")
    val trTime: String,
    @field:NotBlank(message = "trMedium must not be empty")
    @field:Length(min = 2, max = 2, message = "trMedium length are allowed from 8 to 8 characters.")
    val trMedium: String,
    @field:NotBlank(message = "trAmount must not be empty")
    @field:Min(0) @field:Max(9_999_999_999_999)
    val trAmount: Int,
    @field:NotBlank(message = "otherCashierCheckAmount must not be empty")
    @field:Min(0) @field:Max(9_999_999_999_999)
    val selfDrawnBillAmount: Int,
    @field:NotBlank(message = "etcOtherCashierCheckAmount must not be empty")
    @field:Min(0) @field:Max(9_999_999_999_999)
    val etcDrawnBillAmount: Int,
    @field:NotBlank(message = "trBranch must not be empty")
    @field:Length(min = 6, max = 6, message = "trBranch length are allowed from 8 to 8 characters.")
    val trBranch: String,
    @field:NotBlank(message = "depositorName must not be empty")
    @field:Length(min = 6, max = 6, message = "depositorName length are allowed from 6 to 6 characters.")
    val depositorName: String,
    @field:NotBlank(message = "accountNo must not be empty")
    @field:Length(min = 1, max = 16, message = "accountNo length are allowed from 1 to 16 characters.")
    val accountNo: String

)

data class WooriBankManagementRequest(
    @JsonUnwrapped
    @get:Valid
    val management: WooriBankManagementModel,

    @field:NotBlank(message = "accountNo must not be empty")
    @field:Length(min = 1, max = 1, message = "cashDepositYn length are allowed from 1 to 1 characters.")
    val cashDepositYn: String,
    @field:NotBlank(message = "cashierCheckAmount must not be empty")
    @field:Min(0) @field:Max(9_999_999_999_999)
    val selfDrawnCheckAmount: Int,
    @field:NotBlank(message = "branchCode must not be empty")
    @field:Length(min = 7, max = 7, message = "branchCode length are allowed from 7 to 7 characters.")
    val branchCode: String,
)

data class WooriBankManagementResponse(
    @JsonUnwrapped
    val management: WooriBankManagementModel,

    val accountName: String?,
    val accountBalance: Int?,
) : BaseResponse<WooriBankManagementResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<WooriBankManagementResponse> {
        return ResponseEntity(this, HttpStatus.OK)
    }
}

