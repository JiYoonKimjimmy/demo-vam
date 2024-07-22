package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class VirtualAccountCardConnectBatchJobConfig(
    private val virtualAccountCardConnectBatchStep: Step,
    private val fileEncryptionStep: Step,
    private val fileMoveStep: Step,
) {

    @Bean
    fun virtualAccountCardConnectBatchJob(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Job {
        /**
         * 가상 계좌 매핑 Batch Step 수행 과정
         * 1. 가상 계좌 매핑 전문 파일 생성         -- virtualAccountCardConnectBatchStep
         * 2. 신규 생성 전문 파일 내용 암호화       -- fileEncryptionStep
         * 3. NAS 디렉토리 파일 이동              -- fileMoveStep
         */
        return JobBuilder("virtualAccountCardConnectBatchJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(virtualAccountCardConnectBatchStep)
            .on("COMPLETED")
            .to(fileEncryptionStep)
            .on("COMPLETED")
            .to(fileMoveStep)
            .on("COMPLETED")
            .end()
            .end()
            .build()
    }

}