package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.nio.file.Paths

data class VirtualAccountCardFile(
    val filePath: String
) {

    val resource: Resource by lazy {
        UrlResource(Paths.get(filePath).toUri())
    }

}