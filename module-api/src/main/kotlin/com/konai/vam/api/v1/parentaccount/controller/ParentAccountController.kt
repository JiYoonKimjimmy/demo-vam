package com.konai.vam.api.v1.parentaccount.controller

import com.konai.vam.api.v1.parentaccount.controller.model.CreateParentAccountRequest
import com.konai.vam.api.v1.parentaccount.controller.model.CreateParentAccountResponse
import com.konai.vam.api.v1.parentaccount.controller.model.ParentAccountModelMapper
import com.konai.vam.api.v1.parentaccount.service.ParentAccountManagementAdapter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/parent-account")
@RestController
class ParentAccountController(
    private val parentAccountModelMapper: ParentAccountModelMapper,
    private val parentAccountManagementAdapter: ParentAccountManagementAdapter
) {

    @PostMapping
    fun create(@RequestBody request: CreateParentAccountRequest): ResponseEntity<CreateParentAccountResponse> {
        return parentAccountModelMapper.requestToDomain(request)
            .let { parentAccountManagementAdapter.save(it) }
            .let { parentAccountModelMapper.domainToModel(it) }
            .let { CreateParentAccountResponse(it) }
            .success(HttpStatus.CREATED)
    }

}