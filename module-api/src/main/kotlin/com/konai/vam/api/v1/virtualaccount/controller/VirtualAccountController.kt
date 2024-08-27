package com.konai.vam.api.v1.virtualaccount.controller

import com.konai.vam.api.v1.virtualaccount.controller.model.*
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountAdapter
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
    private val virtualAccountAdapter: VirtualAccountAdapter,
    private val virtualAccountModelMapper: VirtualAccountModelMapper
) {

    @PostMapping
    fun create(@RequestBody @Valid request: CreateVirtualAccountRequest): ResponseEntity<CreateVirtualAccountResponse> {
        return virtualAccountModelMapper.requestToDomain(request)
            .let { virtualAccountAdapter.create(it) }
            .let { virtualAccountModelMapper.domainToModel(it) }
            .let { CreateVirtualAccountResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @PostMapping("/all")
    fun findPage(@RequestBody @Valid request: FindAllVirtualAccountRequest = FindAllVirtualAccountRequest()): ResponseEntity<FindAllVirtualAccountResponse> {
        return virtualAccountModelMapper.requestToPredicate(request)
            .let { virtualAccountAdapter.findPage(it, request.pageable) }
            .let { FindAllVirtualAccountResponse(it, virtualAccountModelMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

}