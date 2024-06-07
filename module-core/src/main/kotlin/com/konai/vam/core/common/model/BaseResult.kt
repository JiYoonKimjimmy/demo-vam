package com.konai.vam.core.common.model

import com.konai.vam.core.common.enumerate.ResultStatus
import com.konai.vam.core.common.error.COMPONENT_CODE
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.FeatureCode

class BaseResult(
    private val status: ResultStatus = ResultStatus.SUCCESS,
    private val code: String? = null,
    private var message: String? = null
) {

    fun getStatus() = this.status
    fun getCode() = this.code
    fun getMessage() = this.message

    constructor(featureCode: FeatureCode, errorCode: ErrorCode): this(
        status = ResultStatus.FAILED,
        code = "${COMPONENT_CODE}_${featureCode.code}_${errorCode.code}",
        message = "${featureCode.message}. ${errorCode.message}."
    )

    fun append(message: String?): BaseResult {
        this.message += message?.let { " $message." }
        return this
    }

}