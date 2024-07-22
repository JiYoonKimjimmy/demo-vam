import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItem

class VirtualAccountCardConnectItemFixture {

    fun make(id: Long = 1L): VirtualAccountCardConnectItem {
        return VirtualAccountCardConnectItem(
            id = id,
            serviceId = "123456789012345",
            accountNo = "1234567890123456",
            par = "123456789012345678901234567"
        )
    }

}