package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount

interface VirtualAccountFindAdapter {

    fun findCardConnectedVirtualAccount(accountNo: String): VirtualAccount

}