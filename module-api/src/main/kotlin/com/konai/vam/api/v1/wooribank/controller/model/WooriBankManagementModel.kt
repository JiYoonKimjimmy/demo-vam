package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class WooriBankManagementRequest(
    // common layer
    @field:NotBlank(message = "identifierCode must not be empty")
    @field:Length(min = 1, max = 9, message = "identifierCode length are allowed from 1 to 9 characters.")
    val identifierCode: String,
    @field:NotBlank(message = "companyNo must not be empty")
    @field:Length(min = 1, max = 8, message = "companyNo length are allowed from 1 to 8 characters.")
    val companyNo : String,
    @field:NotBlank(message = "institutionCode must not be empty")
    @field:Length(min = 1, max = 2, message = "institutionCode length are allowed from 1 to 2 characters.")
    val institutionCode : String,
    @field:NotBlank(message = "messageTypeCode must not be empty")
    @field:Length(min = 4, max = 4, message = "messageTypeCode length are allowed from 4 to 4 characters.")
    val messageTypeCode: String,
    @field:NotBlank(message = "businessTypeCode must not be empty")
    @field:Length(min = 3, max = 3, message = "businessTypeCode length are allowed from 3 to 3 characters.")
    val businessTypeCode: String,
    @field:Min(0) @field:Max(0)
    val transmissionCount: Int,
    @field:NotBlank(message = "messageNo must not be empty")
    @field:Length(min = 6, max = 6, message = "messageNo length are allowed from 6 to 6 characters.")
    val messageNo: String,
    @field:NotBlank(message = "transmissionDate must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionDate length are allowed from 6 to 6 characters.")
    val transmissionDate: String,
    @field:NotBlank(message = "transmissionTime must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionTime length are allowed from 6 to 6 characters.")
    val transmissionTime: String,
    @field:Length(min = 4, max = 4, message = "responseCode length are allowed from 4 to 4 characters.")
    val responseCode: String?,
    @field:Length(min = 6, max = 6, message = "orgMessageNo length are allowed from 6 to 6 characters.")
    val orgMessageNo: String?,

    // management layer
    @field:NotBlank(message = "parentAccount must not be empty")
    @field:Length(min = 1, max = 14, message = "parentAccount length are allowed from 1 to 14 characters.")
    val parentAccount: String,
    @field:NotBlank(message = "trDate must not be empty")
    @field:Length(min = 8, max = 8, message = "trDate length are allowed from 8 to 8 characters.")
    val trDate: String,
    @field:NotBlank(message = "trTime must not be empty")
    @field:Length(min = 6, max = 6, message = "trTime length are allowed from 6 to 6 characters.")
    val trTime: String,
    @field:NotBlank(message = "trMedium must not be empty")
    @field:Length(min = 2, max = 2, message = "trMedium length are allowed from 2 to 2 characters.")
    val trMedium: String,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val trAmount: Int,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val otherCashierCheckAmount: Int,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val etcOtherCashierCheckAmount: Int,
    @field:NotBlank(message = "trBranch must not be empty")
    @field:Length(min = 6, max = 6, message = "trBranch length are allowed from 6 to 6 characters.")
    val trBranch: String,
    @field:NotBlank(message = "depositorName must not be empty")
    @field:Length(min = 6, max = 6, message = "depositorName length are allowed from 6 to 6 characters.")
    val depositorName: String,
    @field:NotBlank(message = "accountNo must not be empty")
    @field:Length(min = 1, max = 16, message = "accountNo length are allowed from 1 to 16 characters.")
    val accountNo: String,

    // request layer
    @field:NotBlank(message = "accountNo must not be empty")
    @field:Length(min = 1, max = 1, message = "cashDepositYn length are allowed from 1 to 1 characters.")
    val cashDepositYn: String,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val cashierCheckAmount: Int,
    @field:NotBlank(message = "branchCode must not be empty")
    @field:Length(min = 7, max = 7, message = "branchCode length are allowed from 7 to 7 characters.")
    val branchCode: String,
)

data class WooriBankManagementResponse(
    // common layer
    @field:NotBlank(message = "identifierCode must not be empty")
    @field:Length(min = 1, max = 9, message = "identifierCode length are allowed from 1 to 9 characters.")
    val identifierCode: String,
    @field:NotBlank(message = "companyNo must not be empty")
    @field:Length(min = 1, max = 8, message = "companyNo length are allowed from 1 to 8 characters.")
    val companyNo : String,
    @field:NotBlank(message = "institutionCode must not be empty")
    @field:Length(min = 1, max = 2, message = "institutionCode length are allowed from 1 to 2 characters.")
    val institutionCode : String,
    @field:NotBlank(message = "messageTypeCode must not be empty")
    @field:Length(min = 4, max = 4, message = "messageTypeCode length are allowed from 4 to 4 characters.")
    val messageTypeCode: String,
    @field:NotBlank(message = "businessTypeCode must not be empty")
    @field:Length(min = 3, max = 3, message = "businessTypeCode length are allowed from 3 to 3 characters.")
    val businessTypeCode: String,
    @field:Min(0) @field:Max(0)
    val transmissionCount: Int,
    @field:NotBlank(message = "messageNo must not be empty")
    @field:Length(min = 6, max = 6, message = "messageNo length are allowed from 6 to 6 characters.")
    val messageNo: String,
    @field:NotBlank(message = "transmissionDate must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionDate length are allowed from 6 to 6 characters.")
    val transmissionDate: String,
    @field:NotBlank(message = "transmissionTime must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionTime length are allowed from 6 to 6 characters.")
    val transmissionTime: String,
    @field:Length(min = 4, max = 4, message = "responseCode length are allowed from 4 to 4 characters.")
    val responseCode: String?,
    @field:Length(min = 6, max = 6, message = "orgMessageNo length are allowed from 6 to 6 characters.")
    val orgMessageNo: String?,

    // management layer
    @field:NotBlank(message = "parentAccount must not be empty")
    @field:Length(min = 1, max = 14, message = "parentAccount length are allowed from 1 to 14 characters.")
    val parentAccount: String,
    @field:NotBlank(message = "trDate must not be empty")
    @field:Length(min = 8, max = 8, message = "trDate length are allowed from 8 to 8 characters.")
    val trDate: String,
    @field:NotBlank(message = "trTime must not be empty")
    @field:Length(min = 6, max = 6, message = "trTime length are allowed from 6 to 6 characters.")
    val trTime: String,
    @field:NotBlank(message = "trMedium must not be empty")
    @field:Length(min = 2, max = 2, message = "trMedium length are allowed from 2 to 2 characters.")
    val trMedium: String,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val trAmount: Int,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val otherCashierCheckAmount: Int,
    @field:Min(0) @field:Max(9_999_999_999_999)
    val etcOtherCashierCheckAmount: Int,
    @field:NotBlank(message = "trBranch must not be empty")
    @field:Length(min = 1, max = 6, message = "trBranch length are allowed from 6 to 6 characters.")
    val trBranch: String,
    @field:NotBlank(message = "depositorName must not be empty")
    @field:Length(min = 1, max = 20, message = "depositorName length are allowed from 1 to 20 characters.")
    val depositorName: String,
    @field:NotBlank(message = "accountNo must not be empty")
    @field:Length(min = 1, max = 16, message = "accountNo length are allowed from 1 to 16 characters.")
    val accountNo: String,

    // response layer
    val accountName: String?,
    val accountBalance: String?,
) : BaseResponse<WooriBankManagementResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<WooriBankManagementResponse> {
        return ResponseEntity(this, HttpStatus.OK)
    }
}