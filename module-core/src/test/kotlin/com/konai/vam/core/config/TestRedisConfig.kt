//package com.konai.vam.core.config
//
//import jakarta.annotation.PostConstruct
//import jakarta.annotation.PreDestroy
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.StringRedisSerializer
//import redis.embedded.RedisServer
//
//@Configuration
//@EnableRedisRepositories
//class TestRedisConfig(
//    @Value("\${spring.data.redis.port}") private val redisPort: Int
//) {
//    private val redisServer = RedisServer(redisPort)
//
//    @PostConstruct
//    private fun init() { redisServer.start() }
//
//    @PreDestroy
//    private fun destroy() { redisServer.stop() }
//
//    @Bean
//    fun testRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
//        return RedisTemplate<String, Any>().apply {
//            this.keySerializer = StringRedisSerializer()
//            this.valueSerializer = GenericJackson2JsonRedisSerializer()
//            this.connectionFactory = redisConnectionFactory
//        }
//    }
//
//}