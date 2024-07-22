package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Query
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.orm.JpaQueryProvider

class VirtualAccountCardConnectItemReader(
    entityManagerFactory: EntityManagerFactory,
    batchId: String,
    chunkSize: Int
) : JpaPagingItemReader<VirtualAccountEntity>() {

    init {
        this.name = this::class.java.simpleName
        this.pageSize = chunkSize
        this.setEntityManagerFactory(entityManagerFactory)
        setQueryProvider(VirtualAccountQueryProvider(entityManagerFactory, batchId))
    }

}

class VirtualAccountQueryProvider(
    private val entityManagerFactory: EntityManagerFactory,
    private val batchId: String
) : JpaQueryProvider {

    override fun createQuery(): Query {
        val em = entityManagerFactory.createEntityManager()
        val query = em.createQuery(buildQuery(), VirtualAccountEntity::class.java)
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

    override fun setEntityManager(entityManager: EntityManager) {}

}