package com.konai.vam.api.v1.parentaccount.controller

import com.konai.vam.api.v1.parentaccount.controller.model.*
import com.konai.vam.api.v1.parentaccount.service.ParentAccountFindAdapter
import com.konai.vam.api.v1.parentaccount.service.ParentAccountManagementAdapter
import jakarta.validation.Valid
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
    private val parentAccountManagementAdapter: ParentAccountManagementAdapter,
    private val parentAccountFindAdapter: ParentAccountFindAdapter
) {

    @PostMapping
    fun create(@RequestBody @Valid request: CreateParentAccountRequest): ResponseEntity<CreateParentAccountResponse> {
        return parentAccountModelMapper.requestToDomain(request)
            .let { parentAccountManagementAdapter.save(it) }
            .let { parentAccountModelMapper.domainToModel(it) }
            .let { CreateParentAccountResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @PostMapping("/all")
    fun findAll(@RequestBody @Valid request: FindAllParentAccountRequest = FindAllParentAccountRequest()): ResponseEntity<FindAllParentAccountResponse> {
        return parentAccountModelMapper.requestToPredicate(request)
            .let { parentAccountFindAdapter.findAll(it) }
            .let { FindAllParentAccountResponse(it, parentAccountModelMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

}