package com.konai.vam.batch.v1.filemanagement.batch

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class FileMoveTasklet(
    private val localOutputPath: String,
    private val nasOutputPath: String,
    private val encPrefix: String,
    private val rawPrefix: String,
    private val batchId: String,
    private val rawSuffix: String
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val fileName = "$encPrefix$rawPrefix$batchId$rawSuffix"
        val sourcePath = Paths.get("$localOutputPath/$fileName")
        val targetPath = Paths.get("$nasOutputPath/$fileName")

        contribution.stepExecution.jobExecution.executionContext.putString("outputFileFullPath", "$targetPath")

        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)

        return RepeatStatus.FINISHED
    }

}