package com.konai.vam.api.v1.wooribank.controller

import com.konai.vam.api.v1.wooribank.controller.model.WooriBankManagementModelMapper
import com.konai.vam.api.v1.wooribank.controller.model.WooriBankManagementRequest
import com.konai.vam.api.v1.wooribank.controller.model.WooriBankManagementResponse
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementAdapter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping("/api/v1/woori")
@RestController
class WooriBankManagementController(
    private val wooriBankManagementAdapter: WooriBankManagementAdapter,
    private val wooriBankManagementModelMapper: WooriBankManagementModelMapper,
) {

    @PostMapping("/virtual-account/management")
    fun virtualAccountManagement(@RequestBody @Valid request: WooriBankManagementRequest): ResponseEntity<WooriBankManagementResponse> {
//        return wooriBankManagementModelMapper.requestToDomain(request, RequestContext.get(ContextField.CORRELATION_ID))
        return wooriBankManagementModelMapper.requestToDomain(request, UUID.randomUUID().toString())
            .let { wooriBankManagementAdapter.management(it) }
            .let { wooriBankManagementModelMapper.domainToResponse(it) }
            .success(HttpStatus.OK)
    }

}