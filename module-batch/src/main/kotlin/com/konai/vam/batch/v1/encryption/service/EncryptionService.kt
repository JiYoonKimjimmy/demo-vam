package com.konai.vam.batch.v1.encryption.service

import com.konai.vam.core.common.error
import com.konai.vam.core.restclient.kms.KmsGetEncryptionKeyRequest
import com.konai.vam.core.restclient.kms.KmsRestClient
import jakarta.xml.bind.DatatypeConverter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec

@Service
class EncryptionService(
    private val kmsRestClient: KmsRestClient,
) : EncryptionAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun fetchKmsEncryptKey(batchId: String): String {
        val convertBatchIdToEncrypt = convertBatchIdToEncrypt(batchId)
        val request = KmsGetEncryptionKeyRequest(requestData = convertBatchIdToEncrypt)
        val response = kmsRestClient.getEncryptionKey(request)
        return response.responseData
    }

    private fun convertBatchIdToEncrypt(batchId: String): String {
        val keyDerivationData = if (batchId.length > 32) {
            batchId.substring(0, 32)
        } else if (batchId.length == 32) {
            batchId
        } else {
            batchId + Collections.nCopies((32 - batchId.length), "0")
        }

        return HexFormat.of().formatHex(keyDerivationData.toByteArray())
    }

    override fun encryptFile(key: String, inputFilePath: String, outputFilePath: String) {
        try {
            val cipher = initCipher(key)
            val inputFile = getFile(inputFilePath)
            val outputFile = getFile(outputFilePath)

            FileInputStream(inputFile).use { fileInputStream ->
                FileOutputStream(outputFile).use { fileOutputStream ->
                    CipherOutputStream(fileOutputStream, cipher).use { cipherOutputStream ->
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                            cipherOutputStream.write(buffer, 0, bytesRead)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    private fun initCipher(key: String): Cipher {
        return try {
            val secretKey = SecretKeySpec(DatatypeConverter.parseHexBinary(key), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getFile(path: String): File {
        return Paths.get(path).toFile()
    }

}