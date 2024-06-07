package com.konai.vam.api.exceptionhandler

import com.konai.vam.api.v1.virtualaccount.controller.VirtualAccountController
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.handler.BaseExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [VirtualAccountController::class])
class VirtualAccountControllerExceptionHandler : BaseExceptionHandler(FeatureCode.VIRTUAL_ACCOUNT_SERVICE)