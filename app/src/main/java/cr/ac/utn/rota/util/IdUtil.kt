package cr.ac.utn.rota.util

import java.util.UUID

class IdUtil {
    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
