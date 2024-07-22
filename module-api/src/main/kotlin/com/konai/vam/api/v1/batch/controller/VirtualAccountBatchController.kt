package com.konai.vam.api.v1.batch.controller

import com.konai.vam.api.v1.batch.controller.model.VirtualAccountBulkCardConnectRequest
import com.konai.vam.api.v1.batch.service.VirtualAccountCardBatchAdapter
import com.konai.vam.core.common.model.VoidResponse
import com.konai.vam.core.common.model.success
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import java.io.BufferedInputStream

@RequestMapping("/api/v1/batch")
@RestController
class VirtualAccountBatchController(
    private val virtualAccountCardBatchAdapter: VirtualAccountCardBatchAdapter
) {

    @PostMapping("/virtual-account/card/mapping", "/virtual-account/bulk/card/connect")
    fun virtualAccountBulkCardConnect(@RequestBody request: VirtualAccountBulkCardConnectRequest): ResponseEntity<VoidResponse> {
        virtualAccountCardBatchAdapter.virtualAccountBulkCardConnect(request.batchId, request.serviceId)
        return success(HttpStatus.OK)
    }

    @GetMapping("/virtual-account/card/mapping/file/{batchId}", "/virtual-account/bulk/card/file/{batchId}")
    fun downloadVirtualAccountBulkCardFile(response: HttpServletResponse, @PathVariable batchId: String) {
        val resource = virtualAccountCardBatchAdapter.downloadVirtualAccountBulkCardFile(batchId)

        response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"${resource.filename}\"")
        response.setContentLength(resource.contentLength().toInt())

        FileCopyUtils.copy(BufferedInputStream(resource.inputStream), response.outputStream)
    }

}