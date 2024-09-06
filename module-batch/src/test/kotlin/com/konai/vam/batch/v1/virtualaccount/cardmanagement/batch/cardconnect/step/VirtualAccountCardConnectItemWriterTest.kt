package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import VirtualAccountCardConnectItemFixture
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ExecutionContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class VirtualAccountCardConnectItemWriterTest : BehaviorSpec({

    val virtualAccountCardConnectItemFixture = VirtualAccountCardConnectItemFixture()

    given("가상 계좌 카드 연결 전문 파일 생성 요청하여") {
        val tempDir = Files.createTempDirectory("vam-test")
        val filePath = tempDir.resolve("test_output.txt").toString()
        val batchId = "TEST001"
        val quantity = 1
        val writer = VirtualAccountCardConnectItemWriter(filePath, batchId, quantity)

        val items = Chunk(listOf(virtualAccountCardConnectItemFixture.make()))

        `when`("'Temp' 디렉토리 정상 파일 생성된 경우") {
            writer.open(ExecutionContext())
            writer.write(items)
            writer.close()

            then("파일 내용 정상 확인한다") {
                val lines = Files.readAllLines(Path.of(filePath))
                lines.size shouldBe 3
                lines[0] shouldBe "BatchId=$batchId"
                lines[1] shouldBe "Quantity=00000$quantity"
                lines[2] shouldBe "000001SVCID=123456789012345;PAR=123456789012345678901234567;VIRTUALACC=1234567890123456    ;"

                // Clean up temporary directory
                tempDir.toFile().deleteRecursively()
            }
        }
    }

})