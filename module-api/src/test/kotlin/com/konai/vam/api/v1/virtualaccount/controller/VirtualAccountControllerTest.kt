package com.konai.vam.api.v1.virtualaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.v1.virtualaccount.controller.model.CreateVirtualAccount
import com.konai.vam.api.v1.virtualaccount.controller.model.VirtualAccountModelMapper
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountUseCase
import com.konai.vam.core.common.enumerate.ResultStatus
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
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

    @MockkBean private val virtualAccountUseCase: VirtualAccountUseCase,
    @SpykBean private val virtualAccountModelMapper: VirtualAccountModelMapper,

) : BehaviorSpec({

    given("가상 계좌 등록 요청하면") {
        val accountNumber = "accountNumber001"
        val bankCode = "001"
        val bankName = "우리은행"
        val request = CreateVirtualAccount.Request(
            accountNumber = accountNumber,
            bankCode = bankCode,
            bankName = bankName
        )

        `when`("중복 계좌번호 & 은행코드 예외 발생하는 경우") {
            every { virtualAccountUseCase.create(any()) } throws InternalServiceException(ErrorCode.INTERNAL_SERVER_ERROR)

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
            val id = 1L
            val domain = virtualAccountModelMapper.requestToDomain(request).apply { this.id = id }

            every { virtualAccountUseCase.create(any()) } returns domain

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
                    .andExpect(jsonPath("data.id").value(id))
            }
        }
    }

})