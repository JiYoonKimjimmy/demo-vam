package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class ParentAccountDeleteService(
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountDeleteAdapter {

    override fun delete(id: Long) {
        parentAccountEntityAdapter.delete(id)
    }

}