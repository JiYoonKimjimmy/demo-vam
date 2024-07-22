package com.konai.vam.core.common

import org.springframework.data.domain.Slice

fun <T> Slice<T>.getContentFirstOrNull(): T? {
    return this.content.ifEmpty { listOf(null) }.first()
}

fun <T> ifNotNullEquals(source: T?, target: T?): Boolean {
    return source?.let { target == it } ?: true
}