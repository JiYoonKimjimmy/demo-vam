package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain

import org.springframework.core.io.Resource

data class VirtualAccountCardFile(
    val filePath: String,
    val resource: Resource
)