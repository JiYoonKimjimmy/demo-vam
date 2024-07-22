package com.konai.vam.core.common.converter

import com.konai.vam.core.util.DATE_TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import org.springframework.core.convert.converter.Converter
import java.time.LocalDateTime

class LocalDateTimeToStringConverter : Converter<LocalDateTime, String> {
    override fun convert(source: LocalDateTime): String {
        return source.convertPatternOf(DATE_TIME_BASIC_PATTERN)
    }
}

class StringToLocalDateTimeConverter : Converter<String, LocalDateTime> {
    override fun convert(source: String): LocalDateTime {
        return source.convertPatternOf(DATE_TIME_BASIC_PATTERN)
    }
}