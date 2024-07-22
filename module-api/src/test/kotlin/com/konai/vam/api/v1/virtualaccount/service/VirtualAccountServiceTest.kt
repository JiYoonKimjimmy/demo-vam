package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import fixtures.VirtualAccountEntityFixture
import fixtures.VirtualAccountFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class VirtualAccountServiceTest : BehaviorSpec({

    val virtualAccountEntityAdapter: VirtualAccountEntityAdapter = mockk()
    val virtualAccountMapper: VirtualAccountMapper = mockk()
    val virtualAccountService = VirtualAccountService(virtualAccountEntityAdapter, virtualAccountMapper)
    val virtualAccountFixture = VirtualAccountFixture()
    val virtualAccountEntityFixture = VirtualAccountEntityFixture()

    given("가상 계좌 다건 조회 요청하여") {
        `when`("size 1건 요청하는 경우") {
            val number = 0
            val size = 1
            val predicate = VirtualAccountPredicate()
            val pageableRequest = PageableRequest(number, size)

            val pageable = BasePageable.Pageable(numberOfElements = size)
            val entities = listOf(virtualAccountEntityFixture.make())
            val content = listOf(virtualAccountFixture.make())

            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(pageable, entities)
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

            every { virtualAccountEntityAdapter.findAllByPredicate(any(), any()) } returns BasePageable(pageable, entities)
            every { virtualAccountMapper.entitiesToDomain(any()) } returns BasePageable(pageable, content)

            val result = virtualAccountService.findPage(predicate, pageableRequest)

            then("0건 조회 성공한다") {
                result.pageable.numberOfElements shouldBe 0
                result.content.shouldBeEmpty()
            }
        }
    }

})