package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class WooriBankAggregationModel(
    val aggregateDate: String,
    val konaDepositCount: Int = 0,
    val konaDepositAmount: Long = 0,
    val konaDepositCancelCount: Int = 0,
    val konaDepositCancelAmount: Long = 0,
    val konaDepositTrAmount: Long = 0,
    val bankDepositCount: Int? = null,
    val bankDepositAmount: Long? = null,
    val bankDepositCancelCount: Int? = null,
    val bankDepositCancelAmount: Long? = null,
)

data class WooriBankAggregationRequest(
    @field:NotBlank(message = "requestDate must not be empty")
    @field:Length(min = 8, max = 8, message = "requestDate length are allowed from 8 to 8 characters.")
    val requestDate: String
)

data class WooriBankAggregationResponse(
    val data: WooriBankAggregationModel
) : BaseResponse<WooriBankAggregationResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<WooriBankAggregationResponse> {
        return ResponseEntity(this, httpStatus)
    }
}