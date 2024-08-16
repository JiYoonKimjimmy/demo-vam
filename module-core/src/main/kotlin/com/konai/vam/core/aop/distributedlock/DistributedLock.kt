package com.konai.vam.core.aop.distributedlock

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(

    /**
     * Lock 저장 Cache Key
     */
    val key: String,
    /**
     * Lock 시간 단위
     */
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    /**
     * Lock 획득을 위한 대기 시간
     */
    val waitTime: Long = 5L,
    /**
     * Lock 유지 시간
     */
    val leaseTime: Long = 3L

)
