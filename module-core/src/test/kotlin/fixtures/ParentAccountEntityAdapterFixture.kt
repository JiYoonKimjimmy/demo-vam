package fixtures

import com.konai.vam.core.common.ifNotNullEquals
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import fixtures.TestExtensionFunctions.generateSequence

class ParentAccountEntityAdapterFixture : ParentAccountEntityAdapter {

    private val rows = mutableMapOf<Long, ParentAccountEntity>()

    override fun save(entity: ParentAccountEntity): ParentAccountEntity {
        val id = generateSequence(entity.id)
        rows[id] = entity
        return entity.apply { this.id = id }
    }

    override fun findByPredicate(predicate: ParentAccountPredicate): ParentAccountEntity? {
        return rows
            .values
            .find {
                ifNotNullEquals(predicate.id, it.id)
                && ifNotNullEquals(predicate.parentAccountNo, it.parentAccountNo)
                && ifNotNullEquals(predicate.bankCode, it.bankCode)
            }
    }

    override fun delete(id: Long) {
        rows.remove(id)
    }

    fun findAll(): List<ParentAccountEntity> {
        return rows.values.toList()
    }

    override fun checkDuplicated(parentAccountNo: String, bankCode: String): Boolean {
        return rows.values.any { it.parentAccountNo == parentAccountNo && it.bankCode == bankCode }
    }

    override fun findAllByPredicate(predicate: ParentAccountPredicate, pageableRequest: PageableRequest): BasePageable<ParentAccountEntity?> {
        return rows
            .values
            .filter {
                ifNotNullEquals(predicate.id, it.id)
                && ifNotNullEquals(predicate.parentAccountNo, it.parentAccountNo)
                && ifNotNullEquals(predicate.bankCode, it.bankCode)
            }
            .let { BasePageable(content = it) }
    }

}