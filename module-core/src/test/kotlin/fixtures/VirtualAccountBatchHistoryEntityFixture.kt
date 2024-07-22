package fixtures

import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity

class VirtualAccountBatchHistoryEntityFixture {

    fun make(id: Long? = null, result: Result): VirtualAccountBatchHistoryEntity {
        return VirtualAccountBatchHistoryEntity(
            id = id,
            cardSeBatchId = "12345",
            serviceId = "12345",
            count = 10,
            result = result,
            filePath = ""
        )
    }

}