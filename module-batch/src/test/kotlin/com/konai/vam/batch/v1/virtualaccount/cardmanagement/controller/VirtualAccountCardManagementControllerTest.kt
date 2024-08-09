package com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.controller.model.ConnectBulkCardRequest
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.VirtualAccountCardManagementAdapter
import com.konai.vam.batch.v1.virtualaccount.batchfile.service.VirtualAccountBatchFileDownloadAdapter
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.MethodArgumentNotValidException

@AutoConfigureMockMvc
@WebMvcTest(VirtualAccountCardManagementController::class)
class VirtualAccountCardManagementControllerTest(

    @Autowired val mockMvc: MockMvc,
    @MockkBean private val virtualAccountCardManagementAdapter: VirtualAccountCardManagementAdapter,
    @MockkBean private val virtualAccountBatchFileDownloadAdapter: VirtualAccountBatchFileDownloadAdapter

) : BehaviorSpec({

    given("service id 와 batch id 가 매개변수로 들어오면") {

        `when`("service id 가 유효하지 않은 경우") {
            val batchId = "123456789"
            val serviceId = "123456789123456789123456789"

            val request = ConnectBulkCardRequest(
                batchId = batchId,
                serviceId = serviceId
            )

            then("MethodArgumentNotValidException 예외 발생하여 실패한다") {
                mockMvc.perform(
                    post("/api/v1/batch/internal/virtual-account/bulk/card/connect")
                        .contentType(APPLICATION_JSON)
                        .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest)
                    .andExpect { result ->
                        assert(result.resolvedException is MethodArgumentNotValidException)
                    }
            }
        }

        `when`("batch id 가 유효하지 않은 경우") {
            val batchId = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789"
            val serviceId = "123456789"

            val request = ConnectBulkCardRequest(
                batchId = batchId,
                serviceId = serviceId
            )

            then("MethodArgumentNotValidException 예외 발생하여 실패한다") {
                mockMvc.perform(
                    post("/api/v1/batch/internal/virtual-account/bulk/card/connect")
                        .contentType(APPLICATION_JSON)
                        .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest)
                    .andExpect { result ->
                        assert(result.resolvedException is MethodArgumentNotValidException)
                    }
            }
        }

        `when`("batch id와 service id 가 유효한 경우") {
            val batchId = "123456789"
            val serviceId = "123456789"

            val request = ConnectBulkCardRequest(
                batchId = batchId,
                serviceId = serviceId
            )

            every { virtualAccountCardManagementAdapter.connectBulkCard(any(), any()) } just Runs

            then("배치 매개변수가 유효하지 않다는 예외를 반환한다.") {
                mockMvc.perform(
                    post("/api/v1/batch/internal/virtual-account/bulk/card/connect")
                        .contentType(APPLICATION_JSON)
                        .content(jacksonObjectMapper().writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isOk)
            }
        }
    }

})