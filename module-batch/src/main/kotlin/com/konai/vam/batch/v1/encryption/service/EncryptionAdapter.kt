package com.konai.vam.batch.v1.encryption.service

import org.springframework.stereotype.Service

@Service
interface EncryptionAdapter {

    fun fetchKmsEncryptKey(batchId: String): String

    fun encryptFile(key: String, inputFilePath: String, outputFilePath: String)

}