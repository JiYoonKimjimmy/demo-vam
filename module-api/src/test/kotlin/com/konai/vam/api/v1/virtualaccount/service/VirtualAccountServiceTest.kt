package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.REGISTERED
import com.konai.vam.core.repository.virtualaccount.VirtualAccountRepository
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.security.SecureRandom

class VirtualAccountServiceTest : BehaviorSpec({

    val virtualAccountRepository: VirtualAccountRepository = mockk()
    val virtualAccountMapper: VirtualAccountMapper = mockk()
    val virtualAccountService = VirtualAccountService(virtualAccountRepository, virtualAccountMapper)

    given("가상 계좌 등록 요청하면") {

        `when`("이미 동일한 계좌번호 & 은행코드 정보 등록된 경우") {

            then("중복 가상 계좌 등록 예외 발생하여 실패한다") {

            }
        }

        `when`("등록 가능한 가상 계좌 정보인 경우") {

            then("저장 성공한다") {

            }
        }
    }

    given("가상 계좌 다건 조회 요청하여") {
        `when`("size 1건 요청하는 경우") {
            val number = 0
            val size = 1
            val predicate = VirtualAccountPredicate()
            val pageableRequest = PageableRequest(number, size)

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val entities = listOf(VirtualAccountEntity(id = SecureRandom().nextLong(), EMPTY, EMPTY, FIXATION, REGISTERED))
            val content = listOf(VirtualAccount(id = SecureRandom().nextLong(), EMPTY, EMPTY, FIXATION, REGISTERED))

            every { virtualAccountRepository.findPage(any(), any()) } returns BasePageable(pageable, entities)
            every { virtualAccountMapper.entitiesToDomain(any()) } returns BasePageable(pageable, content)

            val result = virtualAccountService.findPage(predicate, pageableRequest)

            then("1건 조회 성공한다") {
                result.pageable.numberOfElements shouldBe size
                result.content.size shouldBe size
            }
        }

        `when`("조회 결과 없는 경우") {
            val number = 1
            val size = 1
            val predicate = VirtualAccountPredicate()
            val pageableRequest = PageableRequest(number, size)

            val pageable = BasePageable.Pageable()
            val entities = emptyList<VirtualAccountEntity>()
            val content = emptyList<VirtualAccount>()

            every { virtualAccountRepository.findPage(any(), any()) } returns BasePageable(pageable, entities)
            every { virtualAccountMapper.entitiesToDomain(any()) } returns BasePageable(pageable, content)

            val result = virtualAccountService.findPage(predicate, pageableRequest)

            then("0건 조회 성공한다") {
                result.pageable.numberOfElements shouldBe 0
                result.content.shouldBeEmpty()
            }
        }
    }

})