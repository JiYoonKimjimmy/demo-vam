package com.konai.vam.api.v1.virtualaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.api.testsupport.CustomMockMvcTest
import com.konai.vam.api.v1.virtualaccount.controller.model.CreateVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.FindAllVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.FindOneVirtualAccountRequest
import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import fixtures.TestExtensionFunctions.generateUUID
import org.hamcrest.Matchers.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@CustomMockMvcTest
class VirtualAccountControllerTest(
    private val mockMvc: MockMvc,
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : CustomBehaviorSpec({

    val createVirtualAccountUrl = "/api/v1/virtual-account"
    val findOneVirtualAccountUrl = "/api/v1/virtual-account/one"
    val findAllVirtualAccountUrl = "/api/v1/virtual-account/all"

    val objectMapper = jacksonObjectMapper()

    val virtualAccountEntityFixture = virtualAccountEntityFixture()
    val parentAccountEntityFixture = parentAccountEntityFixture()
    lateinit var saved: VirtualAccountEntity

    beforeSpec {
        val accountNo = generateUUID()
        val bankCode = "123"
        val parentAccount = parentAccountEntityFixture.make(null, generateUUID(), bankCode)
        parentAccountEntityAdapter.save(parentAccount)
        val virtualAccount = virtualAccountEntityFixture.make(accountNo = accountNo, bankCode = bankCode, parentAccount = parentAccount)
        saved = virtualAccountEntityAdapter.save(virtualAccount)
    }

    given("가상 계좌 등록 API 요청되어") {
        `when`("가상 계좌 번호 요청 정보가 없는 경우") {
            val request = CreateVirtualAccountRequest(
                accountNo = EMPTY,
                bankCode = "123",
                connectType = FIXATION,
                parentAccountId = 1
            )

            val result = mockMvc
                .post(createVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_901] Virtual Account Service Failed. Argument not valid.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_901"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Argument not valid."))
                        }
                    }
            }
        }

        `when`("'parentAccountId' 기준 모계좌 정보가 없는 요청인 경우") {
            val request = CreateVirtualAccountRequest(
                accountNo = generateUUID(),
                bankCode = "123",
                connectType = FIXATION,
                parentAccountId = 1
            )

            val result = mockMvc
                .post(createVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_023] Virtual Account Service Failed. Parent account not found.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isNotFound() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_023"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Parent account not found."))
                        }
                    }
            }
        }

        `when`("'accountNo' & 'bankCode' 중복 등록 요청인 경우") {
            val request = CreateVirtualAccountRequest(
                accountNo = saved.accountNo,
                bankCode = saved.bankCode,
                connectType = FIXATION,
                parentAccountId = saved.parentAccount.id!!
            )

            val result = mockMvc
                .post(createVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_025] Virtual Account Service Failed. Virtual account is duplicated.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_025"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Virtual account is duplicated."))
                        }
                    }
            }
        }

        `when`("정상 신규 등록 요청인 경우") {
            val accountNo = generateUUID()
            val request = CreateVirtualAccountRequest(
                accountNo = accountNo,
                bankCode = saved.bankCode,
                connectType = FIXATION,
                parentAccountId = saved.parentAccount.id!!
            )

            val result = mockMvc
                .post(createVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("성공 응답 정상 확인한다") {
                result
                    .andExpect {
                        status { isCreated() }
                        content {
                            jsonPath("data.accountNo", equalTo(accountNo))
                            jsonPath("data.bankCode", equalTo(saved.bankCode))
                            jsonPath("data.parentAccountNo", equalTo(saved.parentAccount.parentAccountNo))
                        }
                    }
            }
        }
    }

    given("가상 계좌 단건 조회 API 요청되어") {

        `when`("요청 정보 모두 없는 경우") {
            val request = FindOneVirtualAccountRequest()

            val result = mockMvc
                .post(findOneVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_901] Virtual Account Service Failed. Argument not valid.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_901"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Argument not valid."))
                        }
                    }
            }
        }

        `when`("'accountNo' 요청 정보 있지만, 'bankCode' 요청 정보 없는 경우") {
            val request = FindOneVirtualAccountRequest(accountNo = "1234567890")

            val result = mockMvc
                .post(findOneVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_901] Virtual Account Service Failed. Argument not valid.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_901"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Argument not valid."))
                        }
                    }
            }
        }

        `when`("'bankCode' 요청 정보 있지만, 'accountNo' 요청 정보 없는 경우") {
            val request = FindOneVirtualAccountRequest(bankCode = "123")

            val result = mockMvc
                .post(findOneVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_901] Virtual Account Service Failed. Argument not valid.' 실패 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isBadRequest() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_901"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Argument not valid."))
                        }
                    }
            }
        }

        `when`("'accountNo' 요청 정보 기준 일치한 정보 없는 경우") {
            val request = FindOneVirtualAccountRequest(accountNo = "1234567890", bankCode = "123")

            val result = mockMvc
                .post(findOneVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'[218_1000_001] Virtual Account Service Failed. Virtual account not found.' 실패 결과 응답 정상 확인한다") {
                result
                    .andExpect {
                        status { isNotFound() }
                        content {
                            jsonPath("result.code", equalTo("218_1000_001"))
                            jsonPath("result.message", equalTo("Virtual Account Service Failed. Virtual account not found."))
                        }
                    }
            }
        }

        `when`("'accountNo' 요청 정보 기준 일치한 정보 있는 경우") {
            val request = FindOneVirtualAccountRequest(accountNo = saved.accountNo, bankCode = saved.bankCode)

            val result = mockMvc
                .post(findOneVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("성공 응답 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("data.accountNo", equalTo(saved.accountNo))
                            jsonPath("data.bankCode", equalTo(saved.bankCode))
                            jsonPath("data.parentAccountNo", equalTo(saved.parentAccount.parentAccountNo))
                        }
                    }
            }
        }
    }

    given("가상 계좌 다건 조회 API 요청되어") {

        `when`("'accountNo' 요청 정보와 일치한 정보가 없는 경우") {
            val request = FindAllVirtualAccountRequest(accountNo = "1234567890")

            val result = mockMvc
                .post(findAllVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'0' 건 응답 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", empty<Any>())
                        }
                    }
            }
        }

        `when`("'accountNo' 요청 정보와 일치한 정보 '1' 건 있는 경우") {
            val request = FindAllVirtualAccountRequest(accountNo = saved.accountNo)

            val result = mockMvc
                .post(findAllVirtualAccountUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'1' 건 응답 결과 정상 확인한다") {
                result
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("content", hasSize<Any>(1))
                        }
                    }
            }
        }
    }

})