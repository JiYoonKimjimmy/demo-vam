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
class FileMoveStepConfig(
    @Value("\${batch.virtualAccountCardConnect.file.localPath}") private val localOutputPath: String,
    @Value("\${batch.virtualAccountCardConnect.file.nasPath}") private val nasOutputPath: String,
    @Value("\${batch.virtualAccountCardConnect.file.rawPrefix}") private val rawPrefix: String,
    @Value("\${batch.virtualAccountCardConnect.file.rawSuffix}") private val rawSuffix: String,
    @Value("\${batch.virtualAccountCardConnect.file.encPrefix}") private val encPrefix: String,
) {

    @JobScope
    @Bean
    fun fileMoveStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        @Value("#{jobParameters[batchId]}") batchId: String,
    ): Step {
        return StepBuilder("fileMoveStep", jobRepository)
            .tasklet(FileMoveTasklet(localOutputPath, nasOutputPath, encPrefix, rawPrefix, batchId, rawSuffix), transactionManager)
            .allowStartIfComplete(true)
            .build()
    }

}