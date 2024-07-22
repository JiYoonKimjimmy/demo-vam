//package com.konai.vam.core.config
//
//import com.konai.vam.core.cache.redis.RedisTemplateService
//import io.kotest.matchers.shouldBe
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
//import org.springframework.context.annotation.Import
//import org.springframework.data.redis.core.RedisTemplate
//
//@Import(TestRedisConfig::class)
//@DataRedisTest
//class TestRedisConfigTest(
//    @Autowired
//    private val testRedisTemplate: RedisTemplate<String, Any>
//) {
//
//    private val redisTemplateService = RedisTemplateService(testRedisTemplate)
//
//    @Test
//    fun `Redis 다중 Key 로 Cache 정보 List 조회한다`() {
//        //given
//        val date = "20240624"
//        val key1 = "key1:$date"
//        val value1 = 1
//
//        val key2 = "key2:$date"
//        val value2 = 2
//
//        //when
//        redisTemplateService.setValue(key1, value1)
//        redisTemplateService.setValue(key2, value2)
//
//        //then
//        val result = redisTemplateService.getMultiValue(listOf(key1, key2)) { it as Long }
//
//        result[0] shouldBe value1
//        result[1] shouldBe value2
//    }
//
//}