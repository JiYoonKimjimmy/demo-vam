package com.konai.vam.core.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalTime

class DateUtilKtTest {

    @Test
    fun `LocalDateTime 객체 HHmmss 패턴 변환 테스트`() {
        // given
        val time = LocalTime.of(10, 20, 39)

        // when
        val result = time.convertPatternOf()

        // then
        assertThat(result).isEqualTo("102039")
    }

}