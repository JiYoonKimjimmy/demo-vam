import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import fixtures.TestExtensionFunctions.generateSequence

class ParentAccountEntityFixture {

    fun make(
        id: Long? = generateSequence(),
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