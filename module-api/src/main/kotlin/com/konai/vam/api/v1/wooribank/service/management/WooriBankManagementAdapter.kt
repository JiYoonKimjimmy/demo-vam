package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement

interface WooriBankManagementAdapter {

    fun management(domain: WooriBankManagement): WooriBankManagement

}