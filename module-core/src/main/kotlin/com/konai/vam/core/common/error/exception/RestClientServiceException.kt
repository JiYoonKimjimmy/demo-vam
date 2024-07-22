package com.konai.vam.core.common.error.exception

import com.konai.vam.core.common.error.ErrorCode

class RestClientServiceException(errorCode: ErrorCode) : BaseException(errorCode) {

    constructor(errorCode: ErrorCode, message: String?): this(errorCode) {
        this.detailMessage = message
    }

}