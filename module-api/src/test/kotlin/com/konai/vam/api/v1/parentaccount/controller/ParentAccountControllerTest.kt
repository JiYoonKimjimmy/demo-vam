package com.konai.vam.api.v1.parentaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.parentaccount.controller.model.CreateParentAccountRequest
import fixtures.ExtensionFunctions
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest
class ParentAccountControllerTest(
    private val mockMvc: MockMvc
) : CustomBehaviorSpec({

    val objectMapper = jacksonObjectMapper()

    given("모계좌 등록 API 요청되어") {
        val parentAccountNo = ExtensionFunctions.generateUUID()
        val bankCode = "020"
        val request = CreateParentAccountRequest(parentAccountNo, bankCode)
        val createParentAccountUrl = "/api/v1/parent-account"

        `when`("정상 신규 등록 요청인 경우") {
            val result = mockMvc
                .post(createParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'201 CREATED' 응답 정상 확인한다") {
                result
                    .andExpect {
                        status { isCreated() }
                        content {
                            jsonPath("data.id", greaterThan(0))
                            jsonPath("data.parentAccountNo", equalTo(parentAccountNo))
                        }
                    }
            }
        }

        `when`("중복 등록 요청인 경우") {
            val result = mockMvc
                .post(createParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'400 BAD_REQUEST' 응답 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_3000_024"))
                        }
                    }
            }
        }
    }

})