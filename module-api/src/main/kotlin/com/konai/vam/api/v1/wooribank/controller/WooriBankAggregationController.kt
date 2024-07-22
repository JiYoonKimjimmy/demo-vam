package com.konai.vam.api.v1.wooribank.controller

import com.konai.vam.api.v1.wooribank.controller.model.*
import com.konai.vam.api.v1.wooribank.service.aggregation.WooriBankAggregationAdapter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/woori")
@RestController
class WooriBankAggregationController(
    private val wooribankAggregationAdapter: WooriBankAggregationAdapter,
    private val wooriBankAggregationModelMapper: WooriBankAggregationModelMapper,
) {

    @PostMapping("/virtual-account/aggregation")
    fun aggregateTransaction(@RequestBody @Valid request: WooriBankAggregationRequest): ResponseEntity<WooriBankAggregationResponse> {
        return wooribankAggregationAdapter.aggregateTransaction(aggregateDate = request.requestDate)
            .let { wooriBankAggregationModelMapper.domainToModel(it) }
            .let { WooriBankAggregationResponse(it) }
            .success(HttpStatus.OK)
    }

}