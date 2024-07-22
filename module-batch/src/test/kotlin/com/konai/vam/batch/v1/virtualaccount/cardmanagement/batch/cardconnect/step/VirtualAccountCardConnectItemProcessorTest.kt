package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.VirtualAccountCardConnectItemProcessor
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItemMapper
import fixtures.VirtualAccountEntityFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class VirtualAccountCardConnectItemProcessorTest: BehaviorSpec({

    val virtualAccountEntityFixture = VirtualAccountEntityFixture()

    given("가상계좌 엔티티 클래스가 ItemProcessor에 주어지면") {
        val entity = virtualAccountEntityFixture.make(par = "par")
        var serviceId: String

        `when`("serviceId가 존재할 때") {
            serviceId = "12345678"

            val processor = VirtualAccountCardConnectItemProcessor(VirtualAccountCardConnectItemMapper(), serviceId)
            val result = processor.process(entity)

            then("부가정보 클래스로 변환한다.") {
                result.serviceId shouldBe "12345678"
                result shouldNotBe null
            }
        }
    }
})