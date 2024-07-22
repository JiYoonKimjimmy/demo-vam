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

    @Value("\${batch.virtualAccountCardConnect.file.localPath}")
    private val filePath: String

) {

    @JobScope
    @Bean
    fun virtualAccountCardConnectBatchStep(
        @Value("#{jobParameters[batchId]}") batchId: String,
        @Value("#{jobParameters[serviceId]}") serviceId: String,
        @Value("#{jobParameters[quantity]}") quantity: Int,
        @Value("#{jobParameters[chunkSize]}") chunkSize: Int,
    ): Step {
        return StepBuilder("virtualAccountCardConnectBatchStep", jobRepository)
            .chunk<VirtualAccountEntity, VirtualAccountCardConnectItem>(chunkSize, transactionManager)
            .reader(VirtualAccountCardConnectItemReader(entityManagerFactory, batchId, chunkSize))
            .processor(VirtualAccountCardConnectItemProcessor(virtualAccountCardConnectItemMapper, serviceId))
            .writer(VirtualAccountCardConnectItemWriter(filePath, batchId, quantity))
            .allowStartIfComplete(true)
            .build()
    }

}