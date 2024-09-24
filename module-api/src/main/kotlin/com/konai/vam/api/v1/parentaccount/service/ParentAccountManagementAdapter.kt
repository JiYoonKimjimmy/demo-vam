package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount

interface ParentAccountManagementAdapter {

    fun save(domain: ParentAccount): ParentAccount

    fun update(id: Long, parentAccountNo: String?, bankCode: String?): ParentAccount

}