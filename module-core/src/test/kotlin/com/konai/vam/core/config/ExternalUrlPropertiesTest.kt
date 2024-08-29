package com.konai.vam.core.config

import com.konai.vam.core.common.restclient.ComponentName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExternalUrlPropertiesTest : StringSpec({

    val properties = ExternalUrlProperties()

    "'application-external-url.yml' 설정 'FEP' 정보 정상 확인한다" {
        properties.getProperty(ComponentName.FEP).url shouldBe "http://118.33.122.34:25002"
    }

    "'application-external-url.yml' 설정 'CS' 정보 정상 확인한다" {
        properties.getProperty(ComponentName.CS).url shouldBe "http://118.33.122.28:15820/cs"
    }

})