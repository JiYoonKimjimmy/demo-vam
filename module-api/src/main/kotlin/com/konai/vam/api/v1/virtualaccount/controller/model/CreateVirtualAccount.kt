package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.common.model.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateVirtualAccount {

    data class Request(
        val accountNumber: String,
        val bankCode: String,
        val bankName: String,
    )

    data class Response(
        val data: VirtualAccountModel
    ): BaseResponse<Response>() {
        override fun success(httpStatus: HttpStatus): ResponseEntity<Response> {
            return ResponseEntity(this, httpStatus)
        }
    }

}