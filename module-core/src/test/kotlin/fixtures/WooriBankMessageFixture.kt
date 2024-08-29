package fixtures

import com.konai.vam.core.common.model.wooribank.WooriBankMessage
import com.konai.vam.core.enumerate.WooriBankMessageType

class WooriBankMessageFixture {

    fun make(
        messageCode: WooriBankMessageType.Code,
        messageNo: String,
    ): WooriBankMessage {
        return WooriBankMessage(
            messageTypeCode = messageCode.messageTypeCode,
            businessTypeCode = messageCode.businessTypeCode,
            messageNo = messageNo
        )
    }

}