package com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller.model

import com.konai.vam.batch.v1.virtualaccount.batchfile.service.domain.VirtualAccountBatchFile
import com.konai.vam.core.common.model.BaseResponse
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.File

data class ConnectBulkCardRequest (
    @field:NotBlank(message = "Batch ID must not be empty")
    @field:Length(min = 1, max = 50, message = "Batch ID lengths are allowed from 1 to 50 characters.")
    val batchId: String,
    @field:NotBlank(message = "Service ID must not be empty")
    @field:Length(min = 1, max = 20, message = "Service ID lengths are allowed from 1 to 20 characters.")
    val serviceId: String
)

class DownloadBulkCardFileResponse(
    private val batchFile: VirtualAccountBatchFile
): BaseResponse<Resource>() {

    override fun success(httpStatus: HttpStatus): ResponseEntity<Resource> {
        val file = File(batchFile.filePath)
        val resource = batchFile.resource
        val headers = HttpHeaders().apply {
            add(CONTENT_DISPOSITION, "attachment; filename=\"${file.name}\"")
            add(CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        }
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .body(resource)
    }

}