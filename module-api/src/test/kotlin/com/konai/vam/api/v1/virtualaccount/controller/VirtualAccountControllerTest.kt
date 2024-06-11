package com.konai.vam.api.v1.virtualaccount.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.v1.virtualaccount.controller.model.CreateVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.FindAllVirtualAccountRequest
import com.konai.vam.api.v1.virtualaccount.controller.model.VirtualAccountModelMapper
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountUseCase
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.enumerate.ResultStatus
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountMappingType.FIX
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.enumerate.VirtualAccountStatus.REGISTERED
import com.konai.vam.core.enumerate.YesOrNo.N
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

    given("가상 계좌 등록 요청하였지만") {
        `when`("계좌 번호 요청 정보가 없는 경우") {
            val request = CreateVirtualAccountRequest(
                accountNumber = "",
                bankCode = "001",
                bankName = "bankName",
                mappingType = FIX
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
        val bankName = "우리은행"
        val request = CreateVirtualAccountRequest(
            accountNumber = accountNumber,
            bankCode = bankCode,
            bankName = bankName,
            mappingType = FIX
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

    given("가상 계좌 다건 조회 1건 요청하면") {
        val number = 0
        val size = 1

        `when`("'accountNumber' 일치한 가상 계좌 목록 조회인 경우") {
            val accountNumber = "accountNumber"
            val request = FindAllVirtualAccountRequest(accountNumber = accountNumber, pageable = PageableRequest(number, size))

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val content = listOf(VirtualAccount(1L, accountNumber, EMPTY, EMPTY, FIX, N, REGISTERED))
            every { virtualAccountUseCase.findPage(any(), any()) } returns BasePageable(pageable, content)

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
                    .andExpect(jsonPath("content[0].accountNumber").value(accountNumber))
            }
        }

        `when`("정상 목록 조회인 경우") {
            val request = FindAllVirtualAccountRequest(pageable = PageableRequest(number, size))

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val content = listOf(VirtualAccount(1L, EMPTY, EMPTY, EMPTY, FIX, N, REGISTERED))
            every { virtualAccountUseCase.findPage(any(), any()) } returns BasePageable(pageable, content)

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