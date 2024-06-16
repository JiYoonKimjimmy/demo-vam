package com.konai.vam.core.util

import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

object DBEncryptUtil {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val isEnable = isPossibleEncryption()
    private const val CUBE_ONE = "CUBE_ONE"

    enum class EncryptDataType {
        CUSTOMER_INFO, CARD_INFO
    }

    private fun checkError(errBytes: ByteArray): Boolean {
        val errorStr = String(errBytes, Charset.forName("UTF-8"))
        return !"00000".contentEquals(errorStr)
    }

    private fun cubeOne(text: String, type: EncryptDataType, encryptFlag: Boolean): String? {
        val errBytes = ByteArray(5)
        val result = if (encryptFlag) {
            CubeOneAPI.coencchar(text, type.name, 10, null, null, errBytes)
        } else {
            CubeOneAPI.codecchar(text, type.name, 10, null, null, errBytes)
        }

        return if (checkError(errBytes)) text else result
    }

    private fun isPossibleEncryption(): Boolean {
        return try {
            isCheckEnabledByProperties()
                .also { logger.info("Encrypt DB enable : $it") }
                .also { if (it) encryptionTest() else logger.info("NOT APPLIED - CubeOne Db crypto is not start to applied") }
        } catch (ioe: IOException) {
            throw ioe
        }
    }

    private fun isCheckEnabledByProperties(): Boolean {
        return with(Properties()) {
            this.load(BufferedInputStream(ClassPathResource("application-base.yml").inputStream))
            this["encrypt-provider"].toString() == CUBE_ONE
        }
    }

    private fun encryptionTest() {
        logger.info("APPLIED - CubeOne Db crypto is start to applied")
        CubeOneAPI.jcoinit("API")
        val testText1 = "코나아이"
        val encData1 = this.encrypt(testText1, EncryptDataType.CUSTOMER_INFO)
        logger.info("TEST - CUSTOMER_INFO encryption test [$testText1] -> [$encData1]")
        val decDate1 = this.decrypt(encData1, EncryptDataType.CUSTOMER_INFO)
        logger.info("TEST - CUSTOMER_INFO decryption test [$encData1] -> [$decDate1]")
        val testText2 = "KONAi"
        val encData2 = this.encrypt(testText2, EncryptDataType.CARD_INFO)
        logger.info("TEST - CARD_INFO encryption test [$testText2] -> [$encData2]")
        val decData2 = this.decrypt(encData2, EncryptDataType.CARD_INFO)
        logger.info("TEST - CARD_INFO decryption test [$encData2] -> [$decData2]")
    }

    private fun encrypt(plainText: String, encryptType: EncryptDataType): String =
        if (!isEnable) plainText else cubeOne(plainText, encryptType, true) ?: plainText

    private fun decrypt(encryptedText: String, encryptType: EncryptDataType): String =
        if (!isEnable) encryptedText else cubeOne(encryptedText, encryptType, false) ?: encryptedText

    fun encryptCustomInfo(text: String): String = encrypt(text, EncryptDataType.CUSTOMER_INFO)
    fun decryptCustomInfo(text: String): String = decrypt(text, EncryptDataType.CUSTOMER_INFO)
    fun encryptCardInfo(text: String): String = encrypt(text, EncryptDataType.CARD_INFO)
    fun decryptCardInfo(text: String): String = decrypt(text, EncryptDataType.CARD_INFO)

}