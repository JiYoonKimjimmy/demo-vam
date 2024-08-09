package com.konai.vam.batch.v1.virtualaccount.batchfile.service.domain

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.nio.file.Paths

data class VirtualAccountBatchFile(
    val filePath: String
) {

    val resource: Resource by lazy {
        UrlResource(Paths.get(filePath).toUri())
    }

}