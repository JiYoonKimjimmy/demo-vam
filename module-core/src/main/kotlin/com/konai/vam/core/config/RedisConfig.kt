package com.konai.vam.core.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisSentinelConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@EnableRedisRepositories
@Configuration
class RedisConfig {

    @Autowired
    private lateinit var redisProperties: RedisProperties

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val redisSentinelConfiguration = RedisSentinelConfiguration(
            redisProperties.sentinel.master,
            redisProperties.sentinel.nodes.toSet()
        )
        redisSentinelConfiguration.password = RedisPassword.of(redisProperties.password)
        return LettuceConnectionFactory(redisSentinelConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().setRedisSerializer()
    }

    @Bean
    fun numberRedisTemplate(): RedisTemplate<String, Number> {
        return RedisTemplate<String, Number>().setRedisSerializer()
    }

    private fun <T> RedisTemplate<String, T>.setRedisSerializer(): RedisTemplate<String, T> {
        return apply {
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = GenericJackson2JsonRedisSerializer()
            this.connectionFactory = redisConnectionFactory()
        }
    }

}