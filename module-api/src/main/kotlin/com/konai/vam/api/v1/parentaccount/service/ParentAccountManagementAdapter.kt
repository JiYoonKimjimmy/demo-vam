package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount

interface ParentAccountManagementAdapter {

    fun create(domain: ParentAccount): ParentAccount

    fun update(domain: ParentAccount): ParentAccount

    fun delete(id: Long)

}