package com.konai.vam.api.v1.virtualaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.v1.virtualaccount.controller.model.CreateVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.FindAllVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.VirtualAccountModelMapper
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountAdapter
import com.konai.vam.core.common.enumerate.ResultStatus
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import fixtures.VirtualAccountFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest(VirtualAccountController::class)
class VirtualAccountControllerTest(

    @Autowired private val mockMvc: MockMvc,

    @MockkBean private val virtualAccountAdapter: VirtualAccountAdapter,
    @SpykBean private val virtualAccountModelMapper: VirtualAccountModelMapper,

    ) : BehaviorSpec({

    val virtualAccountFixture = VirtualAccountFixture()

    given("가상 계좌 등록 요청하였지만") {
        `when`("계좌 번호 요청 정보가 없는 경우") {
            val request = CreateVirtualAccountRequest(
                accountNo = "",
                bankCode = "001",
                connectType = FIXATION
            )

            then("MethodArgumentNotValidException 예외 발생하여 실패한다") {
                mockMvc
                    .perform(
                        post("/api/v1/virtual-account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("result.status").value(ResultStatus.FAILED.name))
                    .andExpect(jsonPath("result.code").value("218_1000_901"))
            }
        }
    }

    given("가상 계좌 등록 요청하면") {
        val accountNumber = "accountNumber001"
        val bankCode = "001"
        val request = CreateVirtualAccountRequest(
            accountNo = accountNumber,
            bankCode = bankCode,
            connectType = FIXATION
        )

        `when`("중복 계좌번호 & 은행코드 예외 발생하는 경우") {
            every { virtualAccountAdapter.create(any()) } throws InternalServiceException(ErrorCode.INTERNAL_SERVER_ERROR)

            then("InternalServerException 예외 발생하여 실패한다") {
                mockMvc
                    .perform(
                        post("/api/v1/virtual-account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isInternalServerError)
                    .andExpect(jsonPath("result.status").value(ResultStatus.FAILED.name))
                    .andExpect(jsonPath("result.code").value("218_1000_900"))
            }
        }

        `when`("정상 가상 계좌 등록 정보인 경우") {
            val domain = virtualAccountModelMapper.requestToDomain(request)

            every { virtualAccountAdapter.create(any()) } returns domain

            then("DB 저장하여 정상 응답한다") {
                mockMvc
                    .perform(
                        post("/api/v1/virtual-account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(jsonPath("result.code").isEmpty)
                    .andExpect(jsonPath("result.status").value(ResultStatus.SUCCESS.name))
            }
        }
    }

    given("가상 계좌 다건 조회 1건 요청하면") {
        val number = 0
        val size = 1

        `when`("'accountNo' 일치한 가상 계좌 목록 조회인 경우") {
            val accountNumber = "1234567890"
            val request = FindAllVirtualAccountRequest(accountNumber = accountNumber, pageable = PageableRequest(number, size))

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val content = listOf(virtualAccountFixture.make())
            every { virtualAccountAdapter.findPage(any(), any()) } returns BasePageable(pageable, content)

            then("1건 조회하여 정상 응답한다") {
                mockMvc
                    .perform(
                        post("/api/v1/virtual-account/all")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("result.code").isEmpty)
                    .andExpect(jsonPath("result.status").value(ResultStatus.SUCCESS.name))
                    .andExpect(jsonPath("pageable.numberOfElements").value(size))
                    .andExpect(jsonPath("content[0].accountNo").value(accountNumber))
            }
        }

        `when`("정상 목록 조회인 경우") {
            val request = FindAllVirtualAccountRequest(pageable = PageableRequest(number, size))

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val content = listOf(virtualAccountFixture.make())
            every { virtualAccountAdapter.findPage(any(), any()) } returns BasePageable(pageable, content)

            then("조회 결과 정상 응답한다") {
                mockMvc
                    .perform(
                        post("/api/v1/virtual-account/all")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("result.code").isEmpty)
                    .andExpect(jsonPath("result.status").value(ResultStatus.SUCCESS.name))
                    .andExpect(jsonPath("pageable.numberOfElements").value(size))
            }
        }
    }

})