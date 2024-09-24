package com.konai.vam.api.exception

import com.konai.vam.api.v1.parentaccount.controller.ParentAccountController
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.handler.BaseExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [ParentAccountController::class])
class ParentAccountControllerExceptionHandler : BaseExceptionHandler(FeatureCode.PARENT_ACCOUNT_SERVICE)