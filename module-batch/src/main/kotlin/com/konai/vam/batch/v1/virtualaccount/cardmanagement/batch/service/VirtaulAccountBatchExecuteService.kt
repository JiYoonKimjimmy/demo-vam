package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.service

import com.konai.vam.batch.v1.encryption.service.EncryptionAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VirtaulAccountBatchExecuteService(
    private val jobLauncher: JobLauncher,
    private val virtualAccountCardConnectBatchJob: Job,

    private val encryptionAdapter: EncryptionAdapter,

    @Value("\${batch.virtualAccountCardConnect.config.chunkSize}")
    private val chunkSize: Int

) : VirtaulAccountBatchExecuteAdapter {

    override fun executeCreateSemFileBatchJob(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        val jobParameters = generateJobParameter(batchId, batchHistory.serviceId, batchHistory.count, chunkSize, encryptionAdapter.fetchKmsEncryptKey(batchId))
        val jobExecution = jobLauncher.run(virtualAccountCardConnectBatchJob, jobParameters)
        if (jobExecution.status.isUnsuccessful) throw InternalServiceException(ErrorCode.FAIL_TO_CREATE_BATCH_FILE)
        return jobExecution.executionContext.getString("outputFileFullPath")
    }

    private fun generateJobParameter(batchId: String, serviceId: String, quantity: Int, chunkSize: Int, encryptionKey: String): JobParameters =
        JobParametersBuilder()
            .addString("batchId", batchId)
            .addString("serviceId", serviceId)
            .addLong("quantity", quantity.toLong())
            .addLong("chunkSize", chunkSize.toLong())
            .addString("fileEncryptKey", encryptionKey)
            .addLong("currentTimeStamp", Instant.now().toEpochMilli())
            .toJobParameters()

}