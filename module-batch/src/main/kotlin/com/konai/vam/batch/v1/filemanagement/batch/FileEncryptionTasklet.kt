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
    @Value("\${batch.virtualAccountCardConnect.file.rawPrefix}") private val rawPrefix: String,
    @Value("\${batch.virtualAccountCardConnect.file.rawSuffix}") private val rawSuffix: String,
    @Value("\${batch.virtualAccountCardConnect.file.encPrefix}") private val encPrefix: String,

    private val encryptionAdapter: EncryptionAdapter

) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        return encryptFile()
            .let { this.deleteFile(it) }
            .let { RepeatStatus.FINISHED }
    }

    private fun encryptFile(): String {
        val rawFileName = "$rawPrefix$batchId$rawSuffix"
        val inputFilePath = "$inputPath$rawFileName"
        val outputFileName = "$encPrefix$rawFileName"
        val outputFilePath = "$inputPath$outputFileName"

        encryptionAdapter.encryptFile(fileEncryptKey, inputFilePath, outputFilePath)

        return inputFilePath
    }

    private fun deleteFile(filePath: String) {
        Files.delete(Paths.get(filePath))
    }

}