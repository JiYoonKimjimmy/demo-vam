package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.enumerate.SequenceGeneratorType
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

@SpringBootTest
class SequenceGeneratorServiceLockTest @Autowired constructor(
    val sequenceGeneratorService: SequenceGeneratorService
) : StringSpec({

    afterTest {
        sequenceGeneratorService.deleteSequence(SequenceGeneratorType.WR_BANK, "20240814")
    }

    "'20240814' 우리은행 전문 번호 생성을 위한 Sequence 조회하여 성공 확인한다" {
        // given
        val type = SequenceGeneratorType.WR_BANK
        val date = "20240814"

        // when
        val result = sequenceGeneratorService.getNextSequence(type, date)

        // then
        assertThat(result).isEqualTo("000001")
    }

    "'100건' Sequence 조회 동시성 요청 (without Lock) 결과 정상 확인한다" {
        val measureTimeMillis = measureTimeMillis {
            val type = SequenceGeneratorType.WR_BANK
            val date = "20240814"

            val number = 100
            val executorService = Executors.newFixedThreadPool(number)
            val countDownLatch = CountDownLatch(number)

            println("================= START =================")
            for (i in 1..number) {
                executorService.submit {
                    try {
                        val result = sequenceGeneratorService.getNextSequenceWithoutLock(type, date)
                        println("test=-thread-$i : $result")
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }

            countDownLatch.await()

            println("================== END ==================")

            val result = sequenceGeneratorService.findSequence(type, date)
            assertThat(result).isNotEqualTo("000100")
        }

        println("========== measureTimeMillis : $measureTimeMillis ==========")
    }

    "'100건' Sequence 조회 동시성 요청 결과 정상 확인한다" {
        val measureTimeMillis = measureTime {
            runBlocking {
                val type = SequenceGeneratorType.WR_BANK
                val date = "20240814"
                val number = 100

                println("================= START =================")

                val jobs = List(number) {
                    launch(Dispatchers.Default) {
                        val result = sequenceGeneratorService.getNextSequence(type, date)
                        println("test-coroutine-${it + 1} : $result")
                    }
                }

                jobs.joinAll()

                println("================== END ==================")

                val result = sequenceGeneratorService.findSequence(type, date)
                assertThat(result).isEqualTo("000100")
            }
        }

        println("========== measureTimeMillis : $measureTimeMillis ==========")
    }

})