package com.konai.vam.core.common.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EncryptionCardInfoConverterTest {

    @Test
    fun `DatabaseCustomerInfoColumnConverter 암호화 문자열 생성한다`() {
    	// given
        val plain = "TEST"

    	// when
        val encrypted = EncryptionCardInfoConverter().convertToDatabaseColumn(plain)

    	// then
        assertEquals(plain, encrypted)
    }

    @Test
    fun `DatabaseCustomerInfoColumnConverter 복호화 문자열 생성한다`() {
    	// given
        val plain = "KQpVAa59akgym4G011N8Gg=="

    	// when
        val encrypted = EncryptionCardInfoConverter().convertToEntityAttribute(plain)

    	// then
        assertEquals(plain, encrypted)
    }

}