package com.konai.vam.batch.v1.filemanagement.batch

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class FileEncryptionStepConfig (
    private val fileEncryptionTasklet: FileEncryptionTasklet
) {

    @JobScope
    @Bean
    fun fileEncryptionStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        @Value("#{jobParameters[batchId]}") batchId: String,
        @Value("#{jobParameters[fileEncryptKey]}") fileEncryptKey: String
    ): Step {

        return StepBuilder("fileEncryptionStep", jobRepository)
            .tasklet(fileEncryptionTasklet, transactionManager)
            .allowStartIfComplete(true)
            .build()
    }

}