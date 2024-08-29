package com.konai.vam.batch.v1.encryption.service

import com.konai.vam.batch.v1.encryption.service.EncryptionService.Companion.MAX_BATCH_ID_LENGTH
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class EncryptionServiceTest : StringSpec() {
    init {
        /**
         * batchID : 95336500525500024080912182701I00010
         * convert to HexString :3935333336353030353235353030303234303830393132313832373031493030303130
         * susbstring & upperCase : 3935333336353030353235353030303234303830393132313832373031493030
         * geyKeyResult : 6F1F070F689C04DA0C2ACC682F209C9A23336DE487CD91450ED82E8E484A0A6F
         */
        "Batch 파일 KMS 암호화 Key 생성하여 정상 확인한다" {
            // given
            val batchId = "95336500525500024080912182701I00010"

            // when
            val result = convertBatchIdToEncrypt(batchId)

            // then
            result shouldBe "3935333336353030353235353030303234303830393132313832373031493030"
        }
    }

    private fun convertBatchIdToEncrypt(batchId: String): String {
        val keyDerivationData = when {
            batchId.length > MAX_BATCH_ID_LENGTH -> batchId.take(MAX_BATCH_ID_LENGTH)
            batchId.length < MAX_BATCH_ID_LENGTH -> batchId.padEnd(MAX_BATCH_ID_LENGTH, '0')
            else -> batchId
        }
        return HexFormat.of().formatHex(keyDerivationData.toByteArray())
    }

}