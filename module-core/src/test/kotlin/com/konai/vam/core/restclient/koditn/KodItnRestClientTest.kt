package com.konai.vam.core.restclient.koditn

import com.konai.vam.core.common.error.exception.InternalServiceException
import fixtures.KodItnRestClientModelFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KodItnRestClientTest(
    @Autowired private val kodItnRestClient: KodItnRestClient,
) : BehaviorSpec({

    val kodItnRestClientMock = mockk<KodItnRestClient>()
    val kodItnRestClientModelFixture = KodItnRestClientModelFixture()

    given("정상 서비스ID 요청 정보로") {
        val productId = "953365002513000"
        val request = KodItnGetProductsBasicInfoRequest(productId)

        `when`("KOD_ITN 에 서비스 상품 정보 조회 요청하여") {
            val response = kodItnRestClient.getProductsBasicInfo(request)

            then("정상 응답 성공한다") {
                response shouldNotBe null
                response.productId shouldBe productId
            }
        }
    }

    given("특정 상품ID 요청 정보로") {
        val productId = "123456789"
        val request = KodItnGetProductsBasicInfoRequest(productId)

        `when`("KOD 상품 정책 조회하여 가상 계좌 매핑 '미사용' 정책인 경우") {
            every { kodItnRestClientMock.getProductsBasicInfo(any()) } returns kodItnRestClientModelFixture.make(productId, "00")

            val exception = shouldThrow<InternalServiceException> { kodItnRestClientMock.getProductsBasicInfo(request).checkFixableVirtualAccountPolicy() }

            then("ErrorCode '002' InternalServiceException 발생하여 실패한다") {
                exception.errorCode.code shouldBe "002"
                exception.errorCode.message shouldBe "This serviceId is invalid for connection virtual account"

            }
        }

        `when`("KOD 상품 정책 조회하여 가상 계좌 매핑 '고정' 정책인 경우") {
            every { kodItnRestClientMock.getProductsBasicInfo(any()) } returns kodItnRestClientModelFixture.make(productId, "01", "002")

            val result = kodItnRestClientMock.getProductsBasicInfo(request).checkFixableVirtualAccountPolicy()

            then("가상 계좌 매핑 '01(고정)' 정책 조회 정상 성공한다") {
                result.virtualAccountMappingType shouldBe "01"
            }
        }

        `when`("KOD 상품 정책 조회하여 가상 계좌 매핑 '로테이션' 정책인 경우") {
            every { kodItnRestClientMock.getProductsBasicInfo(any()) } returns kodItnRestClientModelFixture.make(productId, "02")

            val exception = shouldThrow<InternalServiceException> { kodItnRestClientMock.getProductsBasicInfo(request).checkFixableVirtualAccountPolicy() }

            then("ErrorCode '002' InternalServiceException 발생하여 실패한다") {
                exception.errorCode.code shouldBe "002"
                exception.errorCode.message shouldBe "This serviceId is invalid for connection virtual account"
            }
        }
    }

})