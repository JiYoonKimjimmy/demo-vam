package com.konai.vam.core.common.converter

import com.konai.vam.core.util.DBEncryptUtil
import jakarta.persistence.AttributeConverter

class EncryptionCardInfoConverter : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?) = attribute?.let { DBEncryptUtil.encryptCardInfo(it) }

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let { DBEncryptUtil.decryptCardInfo(it) }

}