package com.konai.vam.core.cache.redis

interface RedisTemplateAdaptor {

    fun getNumberMultiValues(keys: List<String>): List<Number>

    fun increment(key: String, value: Long = 1): Long

    fun delete(keys: List<String>): Long

}