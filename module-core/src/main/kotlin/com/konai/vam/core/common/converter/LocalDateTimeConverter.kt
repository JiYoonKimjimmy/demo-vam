package com.konai.vam.core.common.converter

import org.springframework.core.convert.converter.Converter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val DATE_TIME_BASIC_PATTERN = "yyyyMMddHHmmss"

class LocalDateTimeToStringConverter : Converter<LocalDateTime, String> {
    override fun convert(source: LocalDateTime): String {
        return source.format(DateTimeFormatter.ofPattern(DATE_TIME_BASIC_PATTERN))
    }
}

class StringToLocalDateTimeConverter : Converter<String, LocalDateTime> {
    override fun convert(source: String): LocalDateTime {
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATE_TIME_BASIC_PATTERN))
    }
}