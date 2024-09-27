package com.konai.vam.api.v1.virtualaccount.controller

import com.konai.vam.api.v1.virtualaccount.controller.model.*
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountWriteAdapter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/virtual-account")
@RestController
class VirtualAccountController(
    private val virtualAccountModelMapper: VirtualAccountModelMapper,
    private val virtualAccountWriteAdapter: VirtualAccountWriteAdapter,
    private val virtualAccountFindAdapter: VirtualAccountFindAdapter
) {

    @PostMapping
    fun create(@RequestBody @Valid request: CreateVirtualAccountRequest): ResponseEntity<CreateVirtualAccountResponse> {
        return virtualAccountModelMapper.requestToDomain(request)
            .let { virtualAccountWriteAdapter.create(it) }
            .let { virtualAccountModelMapper.domainToModel(it) }
            .let { CreateVirtualAccountResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @PostMapping("/one")
    fun findOne(@RequestBody @Valid request: FindOneVirtualAccountRequest): ResponseEntity<FindOneVirtualAccountResponse> {
        return virtualAccountModelMapper.requestToPredicate(request)
            .let { virtualAccountFindAdapter.findByPredicate(it) }
            .let { virtualAccountModelMapper.domainToModel(it) }
            .let { FindOneVirtualAccountResponse(it) }
            .success(HttpStatus.OK)
    }

    @PostMapping("/all")
    fun findPage(@RequestBody @Valid request: FindAllVirtualAccountRequest = FindAllVirtualAccountRequest()): ResponseEntity<FindAllVirtualAccountResponse> {
        return virtualAccountModelMapper.requestToPredicate(request)
            .let { virtualAccountFindAdapter.findAllByPredicate(it, request.pageable) }
            .let { FindAllVirtualAccountResponse(it, virtualAccountModelMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

}