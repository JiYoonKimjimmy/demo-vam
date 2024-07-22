package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.core.enumerate.WooriBankResponseCode
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class WooriBankCommonModel(
    @field:NotBlank(message = "identifierCode must not be empty")
    @field:Length(min = 1, max = 9, message = "identifierCode length are allowed from 1 to 9 characters.")
    val identifierCode: String,
    @field:NotBlank(message = "companyNo must not be empty")
    @field:Length(min = 1, max = 8, message = "companyNo length are allowed from 1 to 8 characters.")
    val companyNo : String,
    @field:NotBlank(message = "institutionCode must not be empty")
    @field:Length(min = 1, max = 2, message = "institutionCode length are allowed from 1 to 2 characters.")
    val institutionCode : String,
    @field:NotBlank(message = "messageTypeCode must not be empty")
    @field:Length(min = 4, max = 4, message = "messageTypeCode length are allowed from 4 to 4 characters.")
    val messageTypeCode: String,
    @field:NotBlank(message = "businessTypeCode must not be empty")
    @field:Length(min = 3, max = 3, message = "businessTypeCode length are allowed from 3 to 3 characters.")
    val businessTypeCode: String,
    @field:NotBlank(message = "transmissionCount must not be empty")
    @field:Min(0) @field:Max(0)
    val transmissionCount: Int,
    @field:NotBlank(message = "messageNo must not be empty")
    @field:Length(min = 6, max = 6, message = "messageNo length are allowed from 6 to 6 characters.")
    val messageNo: String,
    @field:NotBlank(message = "transmissionDate must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionDate length are allowed from 6 to 6 characters.")
    val transmissionDate: String,
    @field:NotBlank(message = "transmissionTime must not be empty")
    @field:Length(min = 6, max = 6, message = "transmissionTime length are allowed from 6 to 6 characters.")
    val transmissionTime: String,
    @field:Length(min = 4, max = 4, message = "responseCode length are allowed from 4 to 4 characters.")
    val responseCode: WooriBankResponseCode?,
    @field:Length(min = 6, max = 6, message = "orgMessageNo length are allowed from 6 to 6 characters.")
    val orgMessageNo: String?
)