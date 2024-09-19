package com.konai.vam.core.cache.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisTemplateService(
    private val numberRedisTemplate: RedisTemplate<String, Number>,
) : RedisTemplateAdapter {

    override fun getNumberMultiValues(keys: List<String>): List<Number> {
        return numberRedisTemplate.opsForValue().multiGet(keys) ?: emptyList()
    }

    override fun increment(key: String, value: Long): Long {
        return numberRedisTemplate.opsForValue().increment(key, value) ?: 0
    }

    override fun delete(keys: List<String>): Long {
        return numberRedisTemplate.delete(keys)
    }
}