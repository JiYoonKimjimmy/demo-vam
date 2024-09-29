package com.konai.vam.core.common.model

import com.konai.vam.core.common.COMPONENT_CODE
import com.konai.vam.core.common.enumerate.ResultStatus
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.FeatureCode

data class BaseResult(
    val status: ResultStatus = ResultStatus.SUCCESS,
    val code: String? = null,
    val message: String? = null,
    val detailMessage: String? = null,
) {

    constructor(featureCode: FeatureCode, errorCode: ErrorCode, detailMessage: String? = null): this(
        status = ResultStatus.FAILED,
        code = "${COMPONENT_CODE}_${featureCode.code}_${errorCode.code}",
        message = "${featureCode.message} Failed. ${errorCode.message}.",
        detailMessage = detailMessage
    )

}