package com.konai.vam.batch.v1.encryption.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class EncryptionServiceTest : BehaviorSpec({

    val encryptionService = mockk<EncryptionService>()

    given("batchId가 주어질 때") {
        val batchId = "3935333336353030323532393030303233303230363232353130393136493030"

        `when`("비밀키 요청을 보내 요청이 유효할 때") {
            every { encryptionService.fetchKmsEncryptKey(batchId) } returns "secretKey"

            val key = encryptionService.fetchKmsEncryptKey(batchId)

            then("키 값이 존재한다.") {
                key shouldNotBe null
            }
        }

        `when`("비밀키 요청이 유효하지 않을 때") {
            every { encryptionService.fetchKmsEncryptKey(batchId) } throws Exception()

            then("예외를 발생시킨다.") {
                shouldThrow<Exception> {
                    encryptionService.fetchKmsEncryptKey(batchId)
                }
            }
        }
    }

    given("암호키를 취득해온 상태에서") {

        `when`("파일의 암호화에 실패한다면") {

            then("파일 암호화 실패 예외를 반환한다")
        }

        `when`("파일 암호화에 성공한다면") {

            then("파일을 암호화한다.")
        }
    }

})