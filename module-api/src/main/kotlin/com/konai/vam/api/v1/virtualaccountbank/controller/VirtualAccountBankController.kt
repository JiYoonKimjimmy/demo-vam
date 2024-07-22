package com.konai.vam.api.v1.virtualaccountbank.controller

import com.konai.vam.api.v1.virtualaccountbank.controller.model.FindAllVirtualAccountBankRequest
import com.konai.vam.api.v1.virtualaccountbank.controller.model.FindAllVirtualAccountBankResponse
import com.konai.vam.api.v1.virtualaccountbank.controller.model.VirtualAccountBankModelMapper
import com.konai.vam.api.v1.virtualaccountbank.service.VirtualAccountBankFindAdapter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/virtual-account/bank")
@RestController
class VirtualAccountBankController(
    private val virtualAccountBankFindAdapter: VirtualAccountBankFindAdapter,
    private val virtualAccountBankModelMapper: VirtualAccountBankModelMapper
) {

    @PostMapping("/all")
    fun findAllVirtualAccountBank(@RequestBody @Valid request: FindAllVirtualAccountBankRequest): ResponseEntity<FindAllVirtualAccountBankResponse> {
        return virtualAccountBankModelMapper.requestToPredicate(request)
            .let { virtualAccountBankFindAdapter.findAllByPredicate(it) }
            .let { FindAllVirtualAccountBankResponse(it, virtualAccountBankModelMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

}