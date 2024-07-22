package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class VirtualAccountCardConnectItemReaderTest: BehaviorSpec({


    given("get par list from virtual account table") {
        val mockReader = mockk<VirtualAccountCardConnectItemReader>()
        val mockRes = mockk<VirtualAccountEntity>()

        `when`("data exist") {
            every { mockReader.read() } returns mockRes

            then("가져온 값을 반환한다.")
            val result = mockReader.read()

            result shouldBe mockRes
        }

        `when`("data does not exist"){
            every { mockReader.read() } throws InternalServiceException(errorCode = ErrorCode.BATCH_ID_ALREADY_CONNECTED)

            then("MappedParNotFound Exception 을 발생시킨다.") {
                shouldThrow<InternalServiceException> {
                    mockReader.read()
                }
            }
        }

    }
})