package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindService
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountSaveService
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistoryMapper
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnectMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccountbatchhistory.VirtualAccountBatchHistoryEntityAdapter
import fixtures.VirtualAccountBatchHistoryEntityFixture
import fixtures.VirtualAccountEntityFixture
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.security.SecureRandom

class VirtualAccountCardConnectServiceTest : BehaviorSpec({

    val virtualAccountEntityFixture = VirtualAccountEntityFixture()
    val virtualAccountBatchHistoryEntityFixture = VirtualAccountBatchHistoryEntityFixture()

    val virtualAccountMapper = VirtualAccountMapper()
    val virtualAccountEntityAdapter = mockk<VirtualAccountEntityAdapter>()
    val virtualAccountBatchHistoryMapper = VirtualAccountBatchHistoryMapper()
    val virtualAccountBatchHistoryEntityAdapter = mockk<VirtualAccountBatchHistoryEntityAdapter>()
    val virtualAccountBatchHistorySaveAdapter = mockk<VirtualAccountBatchHistorySaveAdapter>()

    val virtualAccountFindService = VirtualAccountFindService(virtualAccountEntityAdapter, virtualAccountMapper)
    val virtualAccountSaveService = VirtualAccountSaveService(virtualAccountEntityAdapter, virtualAccountMapper)
    val virtualAccountCardConnectMapper = VirtualAccountCardConnectMapper()

    val virtualAccountCardConnectService = VirtualAccountCardConnectService(virtualAccountCardConnectMapper, virtualAccountFindService, virtualAccountSaveService, virtualAccountBatchHistorySaveAdapter)

    val id = SecureRandom().nextLong()
    val par = "Q12EA85B4FFF21783695C1C98EA"
    val parList = listOf(par)
    val batchId = "00000800029500017071809205815I00001"
    val serviceId = "000008000295000"
    val virtualAccountEntity = virtualAccountEntityFixture.make()
    val domain = VirtualAccountCardConnect(
        batchId = batchId,
        serviceId = serviceId,
        bankCode = virtualAccountEntity.bankCode,
        pars = parList
    )

    given("실물카드의 목록의 가상계좌 등록 여부를 가상계좌DB에 조회하면") {
        `when`("실물카드의 par에 매핑된 가상계좌 정보가 이미 있으면") {
            val batchEntityWithSuccess = virtualAccountBatchHistoryEntityFixture.make(id = id, result = FAILED)
            val batchHistory = virtualAccountBatchHistoryMapper.entityToDomain(batchEntityWithSuccess)

            every { virtualAccountEntityAdapter.findAllByPars(parList) } returns listOf(virtualAccountEntity)
            every { virtualAccountBatchHistoryEntityAdapter.saveAndFlush(any()) } returns batchEntityWithSuccess
            every { virtualAccountBatchHistorySaveAdapter.save(any()) } returns batchHistory
            every { virtualAccountBatchHistorySaveAdapter.saveAndFlush(any()) } returns batchHistory.copy(reason = ErrorCode.BATCH_ID_ALREADY_CONNECTED.message)
            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(content = arrayListOf(virtualAccountEntity))
            every { virtualAccountEntityAdapter.saveAll(any()) } returns arrayListOf(virtualAccountEntity)

            then("실패 사유가 This batchId has already bean mapped. 이다") {
                val exception = shouldThrow<InternalServiceException> {
                    virtualAccountCardConnectService.connectCardToVirtualAccounts(domain)
                }

                exception.errorCode shouldBe ErrorCode.BATCH_ID_ALREADY_CONNECTED
            }
        }

        `when`("실물카드의 par에 매핑된 가상계좌 정보가 없으면") {
            val batchEntityWithSuccess = virtualAccountBatchHistoryEntityFixture.make(id = id, result = SUCCESS)
            val virtualAccountNotMapped = virtualAccountEntityFixture.make(cardConnectStatus = DISCONNECTED, cardSeBatchId = "batchId")
            val batchHistory = virtualAccountBatchHistoryMapper.entityToDomain(batchEntityWithSuccess)

            // 실물카드의 기 매핑여부 확인을 모킹
            every { virtualAccountEntityAdapter.findAllByPars(any()) } returns emptyList()
            // 매핑되어 있지 않은 가상카드 조회 기능을 모킹
            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(BasePageable.Pageable(), listOf(virtualAccountNotMapped))
            // 매핑되어 있지 않은 가상 카드의 리스트를 저장하는 기능을 모킹
            every { virtualAccountEntityAdapter.saveAll(any()) } returns listOf(virtualAccountNotMapped)
            // 배치 정보 저장 기능을 모킹
            every { virtualAccountBatchHistorySaveAdapter.saveAndFlush(any()) } returns batchHistory
            every { virtualAccountBatchHistoryEntityAdapter.saveAndFlush(any()) } returns batchEntityWithSuccess

            then("InternalServiceException 예외를 발생시키지 않는다.") {
                shouldNotThrow<InternalServiceException> {
                    virtualAccountCardConnectService.connectCardToVirtualAccounts(domain)
                }
              }
        }
    }

    given("가상계좌에 매핑된 par 정보를 가상 계좌 DB에 저장을 시도하는 중") {
        val batchEntityWithSuccess = virtualAccountBatchHistoryEntityFixture.make(id = id, result = SUCCESS)
        val batchHistory = virtualAccountBatchHistoryMapper.entityToDomain(batchEntityWithSuccess)
        val virtualAccountNotMapped = virtualAccountEntityFixture.make(cardConnectStatus = DISCONNECTED, cardSeBatchId = "batchId")

        `when`("가상계좌 매핑에 성공할 때") {

            // 실물카드의 기 매핑여부 확인을 모킹
            every { virtualAccountEntityAdapter.findAllByPars(parList) } returns emptyList()
            // 매핑되어 있지 않은 가상카드 조회 기능을 모킹
            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(BasePageable.Pageable(), listOf(virtualAccountNotMapped))
            // 매핑되어 있지 않은 가상 카드의 리스트를 저장하는 기능을 모킹
            every { virtualAccountEntityAdapter.saveAll(listOf(virtualAccountNotMapped)) } returns listOf(virtualAccountNotMapped)
            // 배치 정보 저장 기능을 모킹
            every { virtualAccountBatchHistoryEntityAdapter.saveAndFlush(any()) } returns batchEntityWithSuccess
            every { virtualAccountBatchHistorySaveAdapter.saveAndFlush(any()) } returns batchHistory

            then("BatchHistory의 reason 이 null 이다") {
                val connectVirtualAccountCard = virtualAccountCardConnectService.connectCardToVirtualAccounts(domain)
                connectVirtualAccountCard.reason shouldBe null
            }
        }

        `when`("가상계좌 갯수가 모자랄 때") {
            // 실물카드의 기 매핑여부 확인을 모킹
            every { virtualAccountEntityAdapter.findAllByPars(any()) } returns emptyList()
            // 가상 계좌가 없는 경우를 모킹
            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(BasePageable.Pageable(), emptyList())
            // 배치 정보 저장 기능을 모킹
            every { virtualAccountBatchHistoryEntityAdapter.saveAndFlush(any()) } returns virtualAccountBatchHistoryEntityFixture.make(id = id, result = FAILED)

            every { virtualAccountBatchHistorySaveAdapter.save(any()) } returns batchHistory
            every { virtualAccountBatchHistorySaveAdapter.saveAndFlush(any()) } returns batchHistory
            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(content = emptyList())

            then("가상 계좌 수가 모자라다는 예외가 발생한다.") {
                val exception = shouldThrow<InternalServiceException> { virtualAccountCardConnectService.connectCardToVirtualAccounts(domain) }
                exception.errorCode.code shouldBe "010"
            }
        }
    }
})