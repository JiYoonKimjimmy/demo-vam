package com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller

import com.konai.vam.batch.v1.virtualaccount.batchfile.service.VirtualAccountBatchFileDownloadAdapter
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller.model.ConnectBulkCardRequest
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller.model.DownloadBulkCardFileResponse
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.VirtualAccountCardManagementAdapter
import com.konai.vam.core.common.model.VoidResponse
import com.konai.vam.core.common.model.success
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/batch/internal/virtual-account")
@RestController
class VirtualAccountCardManagementController(
    private val virtualAccountCardManagementAdapter: VirtualAccountCardManagementAdapter,
    private val virtualAccountBatchFileDownloadAdapter: VirtualAccountBatchFileDownloadAdapter,
) {

    @PostMapping("/bulk/card/connect")
    fun connectBulkCard(@RequestBody @Valid request: ConnectBulkCardRequest): ResponseEntity<VoidResponse> {
        virtualAccountCardManagementAdapter.connectBulkCard(request.batchId, request.serviceId)
        return success(HttpStatus.OK)
    }

    @GetMapping("/bulk/card/download/file/{batchId}")
    fun downloadBulkCardFile(@PathVariable batchId: String): ResponseEntity<Resource> {
        return DownloadBulkCardFileResponse(batchFile = virtualAccountBatchFileDownloadAdapter.downloadBulkCardFile(batchId))
            .success(HttpStatus.OK)
    }

}