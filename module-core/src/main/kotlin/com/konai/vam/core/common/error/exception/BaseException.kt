package com.konai.vam.core.common.error.exception

import com.konai.vam.core.common.error.ErrorCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

open class BaseException(val errorCode: ErrorCode, var detailMessage: String? = null): RuntimeException() {

    fun parseDetailMessage(): String? {
        return detailMessage?.let {
            val jsonString = it.substringAfter(": ").trim('"')
            val json = Json.parseToJsonElement(jsonString).jsonObject
            val reason = json["reason"]?.jsonPrimitive?.content
            val message = json["message"]?.jsonPrimitive?.content
            "[$reason] $message"
        }
    }

}