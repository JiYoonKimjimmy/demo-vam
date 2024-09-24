package com.konai.vam.api.v1.parentaccount.controller

import com.konai.vam.api.v1.parentaccount.controller.model.*
import com.konai.vam.api.v1.parentaccount.service.ParentAccountFindAdapter
import com.konai.vam.api.v1.parentaccount.service.ParentAccountManagementAdapter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
            .let { parentAccountManagementAdapter.create(it) }
            .let { parentAccountModelMapper.domainToModel(it) }
            .let { CreateParentAccountResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @PostMapping("/all")
    fun findAll(@RequestBody @Valid request: FindAllParentAccountRequest?): ResponseEntity<FindAllParentAccountResponse> {
        return parentAccountModelMapper.requestToPredicate(request)
            .let { parentAccountFindAdapter.findAll(it) }
            .let { FindAllParentAccountResponse(it, parentAccountModelMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

    @PutMapping("/{parentAccountId}")
    fun update(
        @PathVariable parentAccountId: Long,
        @RequestBody @Valid request: UpdateParentAccountRequest?
    ): ResponseEntity<UpdateParentAccountResponse> {
        return parentAccountModelMapper.requestToDomain(parentAccountId, request)
            .let { parentAccountManagementAdapter.update(it) }
            .let { parentAccountModelMapper.domainToModel(it) }
            .let { UpdateParentAccountResponse(it) }
            .success(HttpStatus.OK)
    }

}