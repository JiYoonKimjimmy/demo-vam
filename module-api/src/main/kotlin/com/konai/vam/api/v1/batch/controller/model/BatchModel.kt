package com.konai.vam.api.v1.batch.controller.model

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class VirtualAccountBulkCardConnectRequest (

    @field:NotBlank(message = "Batch ID must not be empty")
    @field:Length(min = 1, max = 50, message = "Batch ID lengths are allowed from 1 to 50 characters.")
    val batchId: String,
    @field:NotBlank(message = "Service ID must not be empty")
    @field:Length(min = 1, max = 20, message = "Service ID lengths are allowed from 1 to 20 characters.")
    val serviceId: String

)