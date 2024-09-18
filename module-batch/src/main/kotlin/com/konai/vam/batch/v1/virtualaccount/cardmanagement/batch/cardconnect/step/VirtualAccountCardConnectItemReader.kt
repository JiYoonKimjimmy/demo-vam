package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Query
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider

class VirtualAccountCardConnectItemReader(
    entityManagerFactory: EntityManagerFactory,
    batchId: String,
    chunkSize: Int
) : JpaPagingItemReader<VirtualAccountEntity>() {

    init {
        this.name = this::class.java.simpleName
        this.pageSize = chunkSize
        this.setEntityManagerFactory(entityManagerFactory)
        this.setQueryProvider(VirtualAccountQueryProvider(batchId))
    }

}

class VirtualAccountQueryProvider(private val batchId: String) : AbstractJpaQueryProvider() {

    override fun createQuery(): Query {
        val query = entityManager.createQuery(buildQuery(), VirtualAccountEntity::class.java)
        if (batchId.isNotBlank()) {
            query.setParameter("batchId", batchId)
        }
        return query
    }

    private fun buildQuery(): String {
        return """
            SELECT va FROM VirtualAccount va
            WHERE 1=1
            AND va.par is not null
            ${if (batchId.isNotBlank()) "AND va.cardSeBatchId = :batchId" else EMPTY}
        """.trimIndent()
    }

    override fun afterPropertiesSet() {}

}