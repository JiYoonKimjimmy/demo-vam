package com.konai.vam.core.common.converter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverterTest {

    @Test
    fun `LocalDateTime 을 'yyyyMMddHHmmSS' 패턴의 String 으로 변환한다`() {
    	// given
        val date = "20240616093130"
        val localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_BASIC_PATTERN))

    	// when
        val result = LocalDateTimeToStringConverter().convert(localDateTime)

        // then
        assertThat(result).isEqualTo(date)
    }

    @Test
    fun `'yyyyMMddHHmmSS' 패턴의 String 을 LocalDateTime 으로 변환한다`() {
        // given
        val date = "20240616093130"

        // when
        val result = StringToLocalDateTimeConverter().convert(date)

        // then
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 6, 16, 9, 31, 30))
    }

}