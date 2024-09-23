package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount

interface ParentAccountAdapter {

    fun save(domains: ParentAccount): ParentAccount

}