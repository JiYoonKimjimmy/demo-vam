package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.BaseResponse
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindOneVirtualAccountRequest(
    @field:Length(min = 1, max = 20, message = "Account number lengths are allowed from 1 to 20 characters.")
    val accountNo: String? = null,
    @field:Length(min = 3, max = 3, message = "Bank code lengths are allowed from 3 to 3 characters.")
    val bankCode: String? = null,
    @field:Length(min = 27, max = 27, message = "Par lengths are allowed from 27 to 27 characters.")
    val par: String? = null,
) {
    fun validation(): FindOneVirtualAccountRequest {
        return if (
            (accountNo.isNullOrEmpty() && par.isNullOrEmpty())
            || (accountNo?.isNotEmpty() == true && bankCode.isNullOrEmpty())
            || (bankCode?.isNotEmpty() == true && accountNo.isNullOrEmpty())
        ) {
            throw InternalServiceException(ErrorCode.ARGUMENT_NOT_VALID_ERROR)
        } else {
            this
        }
    }
}

data class FindOneVirtualAccountResponse(
    val data: VirtualAccountModel
) : BaseResponse<FindOneVirtualAccountResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<FindOneVirtualAccountResponse> {
        return ResponseEntity(this, httpStatus)
    }
}