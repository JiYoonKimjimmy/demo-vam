package com.konai.vam.core.common.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EncryptionCustomerInfoConverterTest {

    @Test
    fun `DatabaseCustomerInfoColumnConverter 암호화 문자열 생성한다`() {
    	// given
        val plain = "TEST"

    	// when
        val encrypted = EncryptionCustomerInfoConverter().convertToDatabaseColumn(plain)

    	// then
        assertEquals("TEST", encrypted)
    }

    @Test
    fun `DatabaseCustomerInfoColumnConverter 복호화 문자열 생성한다`() {
    	// given
        val plain = "vyZ6+Un/rWus6N6RB+va2A=="

    	// when
        val encrypted = EncryptionCustomerInfoConverter().convertToEntityAttribute(plain)

    	// then
        assertEquals("vyZ6+Un/rWus6N6RB+va2A==", encrypted)
    }

}