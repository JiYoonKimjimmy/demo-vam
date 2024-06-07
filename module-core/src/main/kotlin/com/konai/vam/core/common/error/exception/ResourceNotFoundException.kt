package com.konai.vam.core.common.error.exception

import com.konai.vam.core.common.error.ErrorCode

class ResourceNotFoundException(errorCode: ErrorCode) : BaseException(errorCode)