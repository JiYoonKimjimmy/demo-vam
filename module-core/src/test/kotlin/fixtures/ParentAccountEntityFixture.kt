package fixtures

import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity

class ParentAccountEntityFixture {

    fun make(
        id: Long? = ExtensionFunctions.generateSequence(),
        parentAccountNo: String,
        bankCode: String
    ): ParentAccountEntity {
        return ParentAccountEntity(
            id = id,
            parentAccountNo = parentAccountNo,
            bankCode = bankCode
        )
    }

}