package com.konai.vam.api.exception

import com.konai.vam.api.v1.virtualaccountbank.controller.VirtualAccountBankController
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.handler.BaseExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [VirtualAccountBankController::class])
class VirtualAccountBankControllerExceptionHandler : BaseExceptionHandler(FeatureCode.VIRTUAL_ACCOUNT_BANK_SERVICE)