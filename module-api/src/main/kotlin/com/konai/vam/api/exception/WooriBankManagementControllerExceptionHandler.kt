package com.konai.vam.api.exception

import com.konai.vam.api.v1.wooribank.controller.WooriBankManagementController
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.handler.BaseExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [WooriBankManagementController::class])
class WooriBankManagementControllerExceptionHandler : BaseExceptionHandler(FeatureCode.WOORI_BANK_MANAGEMENT_SERVICE)