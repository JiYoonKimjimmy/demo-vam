package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import VirtualAccountCardConnectItemFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ExecutionContext
import java.nio.file.Files
import java.nio.file.Paths

class VirtualAccountCardConnectItemWriterTest : BehaviorSpec({

    val virtualAccountCardConnectItemFixture = VirtualAccountCardConnectItemFixture()

    given("VirtualAccountCardConnectItemWriter") {
        val batchId = "00000800029500017071809205815I00001"
        val quantity = 1

        val tempDir = Paths.get(System.getProperty("java.io.tmpdir"))
        val tempFile = tempDir.resolve("raw_data_additional_$batchId.SEM")
        val writer = VirtualAccountCardConnectItemWriter(tempDir.toString(), batchId, quantity)

        `when`("writing data to a file") {

            writer.open(ExecutionContext())
            writer.write(Chunk(listOf(virtualAccountCardConnectItemFixture.make())))
            writer.close()

            then("부가전문에 대한 데이터를 포멧에 맞춰 파일이 갖고 있다.") {
                val lines = Files.readAllLines(tempFile)
                lines[0] shouldBe "BatchId=$batchId"
                lines[1] shouldBe "Quantity=00000$quantity"
                lines[2] shouldBe "000001SVCID=123456789012345;par=123456789012345678901234567;VIRTUALACC=1234567890123456;"
            }
        }
    }

    given("암호화 키로 평문의 부가정보를 암호화하려할 때") {
        `when`("키 값이 유효하지 않을 때") {
            then("유효한 키 값이 아니라는 예외를 발생시킨다.")
        }


        `when`("암호화가 성공하면") {

            then("정상응답을 반환한다.") {

            }
        }

        `when`("암호화에 실패하면") {

            then("암호화 실패 예외를 반환한다.") {

            }
        }

    }

})