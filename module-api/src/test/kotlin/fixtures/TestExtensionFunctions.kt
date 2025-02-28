package fixtures

import java.security.SecureRandom
import java.util.UUID

object TestExtensionFunctions {

    fun generateUUID(length: Int = 10): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length)
    }

    fun generateSequence(id: Long? = null): Long {
        return id ?: SecureRandom().nextLong()
    }

}