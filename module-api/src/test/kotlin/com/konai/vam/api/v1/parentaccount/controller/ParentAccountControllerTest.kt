package com.konai.vam.api.v1.parentaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.parentaccount.controller.model.CreateParentAccountRequest
import com.konai.vam.api.v1.parentaccount.controller.model.FindAllParentAccountRequest
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import fixtures.TestExtensionFunctions.generateUUID
import org.hamcrest.Matchers.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ParentAccountControllerTest(
    private val mockMvc: MockMvc,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : CustomBehaviorSpec({

    val objectMapper = jacksonObjectMapper()
    val parentAccountEntityFixture = parentAccountEntityFixture()

    val saveParentAccountEntity: (String, String) -> (Unit) = { parentAccountNo, bankCode ->
        val entity = parentAccountEntityFixture.make(parentAccountNo = parentAccountNo, bankCode = bankCode)
        parentAccountEntityAdapter.save(entity)
    }

    given("모계좌 등록 API 요청되어") {
        val parentAccountNo = generateUUID()
        val bankCode = "123"
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
                            jsonPath("data.parentAccountId", greaterThan(0))
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

    given("모계좌 다건 조회 API 요청되어") {
        val parentAccountNo = generateUUID()
        val bankCode = "123"
        val findAllParentAccountUrl = "/api/v1/parent-account/all"

        `when`("'parentAccountNo' 기준 조회 요청이나 등록 정보 없는 경우") {
            val request = FindAllParentAccountRequest(parentAccountNo, null)
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'content' 응답 결과 Empty 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", empty<Any>())
                        }
                    }
            }
        }

        // 모계좌 정보 저장
        saveParentAccountEntity(parentAccountNo, bankCode)

        `when`("'parentAccountNo' 기준 조회 요청인 경우") {
            val request = FindAllParentAccountRequest(parentAccountNo, null)
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'content' 응답 결과 '1건' 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", hasSize<Any>(1))
                        }
                    }
            }
        }

        // 모계좌 정보 추가 저장
        saveParentAccountEntity(generateUUID(), bankCode)

        `when`("'bankCode' 기준 조회 요청인 경우") {
            val request = FindAllParentAccountRequest(null, bankCode)
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'content' 응답 결과 '2건' 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", hasSize<Any>(2))
                        }
                    }
            }
        }

        // 모계좌 정보 추가 저장
        saveParentAccountEntity(generateUUID(), "999")

        `when`("요청 정보가 모두 'null' 인 요청인 경우") {
            val request = FindAllParentAccountRequest(null, null)
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'content' 응답 결과 '3건' 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", hasSize<Any>(greaterThan(3)))
                        }
                    }
            }
        }

        `when`("요청 정보 없이 요청인 경우") {
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                }
                .andDo { print() }

            then("'content' 응답 결과 '3건' 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", hasSize<Any>(greaterThan(3)))
                        }
                    }
            }
        }

        `when`("'parentAccountNo' 요청 정보가 '20글자' 이상인 요청인 경우") {
            val request = FindAllParentAccountRequest(generateUUID(30), null)
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'218_3000_901' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_3000_901"))
                            jsonPath("result.detailMessage", equalTo("Parent account number lengths are allowed from 1 to 20 characters."))
                        }
                    }
            }
        }

        `when`("'bankCode' 요청 정보가 '3글자' 아닌 요청인 경우") {
            val request = FindAllParentAccountRequest(null, "01")
            val result = mockMvc
                .post(findAllParentAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'218_3000_901' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_3000_901"))
                            jsonPath("result.detailMessage", equalTo("Bank code must be exactly 3 digits."))
                        }
                    }
            }
        }

    }

})