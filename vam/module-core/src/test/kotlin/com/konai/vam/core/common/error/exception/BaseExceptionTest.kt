package com.konai.vam.core.common.error.exception

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.kotest.core.spec.style.FreeSpec
import org.junit.jupiter.api.Assertions.assertEquals

class BaseExceptionTest : FreeSpec({

    "외부 연동 API 에러 메시지 변환하여 'reason' & 'message' 추출 확인한다" - {
        val detailMessage = "400 Bad Request: \"{\"par\":\"Q1E39E5C50F1DE90B6F502C607F\",\"reason\":\"24_4000_510\",\"message\":\"AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT\",\"transactionId\":\"20240809125859002914\",\"rechargerId\":\"4101098153365CX\",\"authYn\":\"X\",\"nrNumber\":null,\"amount\":200000,\"success\":false}\""
        
        val jsonString = detailMessage.substringAfter(": ").trim('"')
        val json = Json.parseToJsonElement(jsonString).jsonObject
        
        val reason = json["reason"]?.jsonPrimitive?.content
        val message = json["message"]?.jsonPrimitive?.content
        
        println("Reason: $reason")
        println("Message: $message")
        
        assertEquals("24_4000_510", reason)
        assertEquals("AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT", message)
    }
    
})