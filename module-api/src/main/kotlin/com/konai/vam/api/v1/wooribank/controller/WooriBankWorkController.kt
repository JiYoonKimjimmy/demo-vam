package com.konai.vam.api.v1.wooribank.controller

import com.konai.vam.api.v1.wooribank.service.work.WooriBankWorkAdapter
import com.konai.vam.core.common.model.VoidResponse
import com.konai.vam.core.common.model.success
import com.konai.vam.core.enumerate.WooriBankMessageType.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/woori/work")
@RestController
class WooriBankWorkController(
    private val wooribankWorkAdapter: WooriBankWorkAdapter,
) {

    @PostMapping("/start")
    fun start(): ResponseEntity<VoidResponse> {
        wooribankWorkAdapter.work(WORK_START)
        return success(HttpStatus.OK)
    }

    @PostMapping("/stop")
    fun stop(): ResponseEntity<VoidResponse> {
        wooribankWorkAdapter.work(WORK_STOP)
        return success(HttpStatus.OK)
    }

    @PostMapping("/failures")
    fun failures(): ResponseEntity<VoidResponse> {
        wooribankWorkAdapter.work(WORK_FAILURES)
        return success(HttpStatus.OK)
    }

    @PostMapping("/failures/resolved")
    fun failuresResolved(): ResponseEntity<VoidResponse> {
        wooribankWorkAdapter.work(WORK_FAILURES_RESOLVED)
        return success(HttpStatus.OK)
    }

}