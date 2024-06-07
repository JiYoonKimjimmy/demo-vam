package com.konai.vam.core.common.converter

import com.konai.vam.core.util.DBEncryptUtil
import jakarta.persistence.AttributeConverter

class DatabaseCustomerInfoColumnConverter : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?) = attribute?.let { DBEncryptUtil.encryptCustomInfo(it) }

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let { DBEncryptUtil.decryptCustomInfo(it) }

}