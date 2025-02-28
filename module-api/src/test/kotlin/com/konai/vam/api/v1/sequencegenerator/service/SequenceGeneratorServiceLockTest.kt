package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.enumerate.SequenceGeneratorType
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory
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
    // logger
    val logger = LoggerFactory.getLogger(this::class.java)

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
        assertThat(result).isEqualTo(1)
    }

    "'100건' Sequence 조회 동시성 요청 (without Lock) 결과 정상 확인한다" {
        val measureTimeMillis = measureTimeMillis {
            val type = SequenceGeneratorType.WR_BANK
            val date = "20240814"

            val number = 100
            val executorService = Executors.newFixedThreadPool(number)
            val countDownLatch = CountDownLatch(number)

            logger.info("================= START =================")

            for (i in 1..number) {
                executorService.submit {
                    try {
                        val result = sequenceGeneratorService.getNextSequenceWithoutLock(type, date)
                        logger.info("test=-thread-$i : $result")
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }

            countDownLatch.await()

            logger.info("================== END ==================")

            val result = sequenceGeneratorService.findSequence(type, date)
            assertThat(result).isNotEqualTo(100)
        }

        logger.info("========== measureTimeMillis : $measureTimeMillis ==========")
    }

    "'100건' Sequence 조회 동시성 요청 결과 정상 확인한다" {
        val measureTimeMillis = measureTime {
            val type = SequenceGeneratorType.WR_BANK
            val date = "20240814"
            val number = 100

            runBlocking {
                logger.info("================= START [with LOCK] =================")

                repeat(number) {
                    launch(Dispatchers.IO) {
                        val result = sequenceGeneratorService.getNextSequence(type, date)
                        logger.info("test-coroutine-${it + 1} : $result")
                    }
                }

                logger.info("================== END [with LOCK] ==================")
            }

            val result = sequenceGeneratorService.findSequence(type, date)
            assertThat(result).isEqualTo(100)
        }

        logger.info("========== measureTimeMillis [with LOCK] : $measureTimeMillis ==========")
    }

})