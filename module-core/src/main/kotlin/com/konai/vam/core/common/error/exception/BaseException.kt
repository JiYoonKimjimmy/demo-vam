package com.konai.vam.core.common.error.exception

import com.konai.vam.core.common.error.ErrorCode

open class BaseException(val errorCode: ErrorCode, var detailMessage: String? = null): RuntimeException()