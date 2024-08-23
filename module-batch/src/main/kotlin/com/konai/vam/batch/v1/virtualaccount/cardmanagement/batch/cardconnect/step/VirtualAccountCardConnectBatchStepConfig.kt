package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItem
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItemMapper
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class VirtualAccountCardConnectBatchStepConfig(

    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val virtualAccountCardConnectItemMapper: VirtualAccountCardConnectItemMapper,

    @Value("\${batch.virtualAccountCardConnect.file.localPath}") private val localPath: String,
    @Value("\${batch.virtualAccountCardConnect.file.rawPrefix}") private val rawPrefix: String,
    @Value("\${batch.virtualAccountCardConnect.file.rawSuffix}") private val rawSuffix: String,

) {

    @JobScope
    @Bean
    fun virtualAccountCardConnectBatchStep(
        @Value("#{jobParameters[batchId]}") batchId: String,
        @Value("#{jobParameters[chunkSize]}") chunkSize: Int,
        @Value("#{jobParameters[serviceId]}") serviceId: String,
        @Value("#{jobParameters[quantity]}") quantity: Int,
    ): Step {
        return StepBuilder("virtualAccountCardConnectBatchStep", jobRepository)
            .chunk<VirtualAccountEntity, VirtualAccountCardConnectItem>(chunkSize, transactionManager)
            .reader(reader(batchId, chunkSize))
            .processor(processor(serviceId))
            .writer(writer(batchId, quantity))
            .allowStartIfComplete(true)
            .build()
    }

    private fun reader(batchId: String, chunkSize: Int): VirtualAccountCardConnectItemReader {
        return VirtualAccountCardConnectItemReader(entityManagerFactory, batchId, chunkSize)
    }

    private fun processor(serviceId: String): VirtualAccountCardConnectItemProcessor {
        return VirtualAccountCardConnectItemProcessor(virtualAccountCardConnectItemMapper, serviceId)
    }

    private fun writer(batchId: String, quantity: Int): VirtualAccountCardConnectItemWriter {
        val filePath = "$localPath$rawPrefix$batchId$rawSuffix"
        return VirtualAccountCardConnectItemWriter(filePath, batchId, quantity)
    }

}