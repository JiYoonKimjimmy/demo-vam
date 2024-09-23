package fixtures

import java.util.UUID

fun generateUUID(length: Int = 10): String {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length)
}