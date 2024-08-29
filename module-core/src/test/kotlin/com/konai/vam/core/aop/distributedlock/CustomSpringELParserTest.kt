package com.konai.vam.core.aop.distributedlock

import com.konai.vam.core.enumerate.SequenceGeneratorType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CustomSpringELParserTest {

    @Test
    fun `SpEL 문자 변환 테스트 성공 확인한다`() {
        // given
        val parameterNames: Array<String> = arrayOf("type", "date")
        val args: Array<Any> = arrayOf(SequenceGeneratorType.WR_BANK, "20240814")
        val key = "#type + ':' + #date"

        // when
        val result = CustomSpringELParser.getDynamicValue(
            parameterNames,
            args,
            key
        )

        // then
        assertThat(result).isEqualTo("WR_BANK:20240814")
    }

}