package com.konai.vam.batch.v1.filemanagement.batch

import com.konai.vam.batch.v1.encryption.service.EncryptionAdapter
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths


@StepScope
@Component
class FileEncryptionTasklet(

    @Value("#{jobParameters[batchId]}") private val batchId: String,
    @Value("#{jobParameters[fileEncryptKey]}") private val fileEncryptKey: String,
    @Value("\${batch.virtualAccountCardConnect.file.localPath}") private val inputPath: String,

    private val encryptionAdapter: EncryptionAdapter

) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val rawFileName = "raw_data_additional_$batchId.SEM"

        val outputFileName = "enc_${rawFileName}"

        val inputFilePath = "${inputPath}${rawFileName}"
        val outputFilePath = "${inputPath}${outputFileName}"

        encryptionAdapter.encryptFile(fileEncryptKey, inputFilePath, outputFilePath)

        Files.delete(Paths.get(inputFilePath))

        return RepeatStatus.FINISHED
    }
}