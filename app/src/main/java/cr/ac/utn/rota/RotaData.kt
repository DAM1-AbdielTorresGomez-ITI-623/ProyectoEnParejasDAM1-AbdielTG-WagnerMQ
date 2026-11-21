package cr.ac.utn.rota

import cr.ac.utn.rota.data.memory.MemoryDataRepository
import cr.ac.utn.rota.domain.repository.IRotaRepository
import cr.ac.utn.rota.util.IdUtil

object RotaData {
    val repository: IRotaRepository = MemoryDataRepository()
    val activeUserId = IdUtil.generateId()
}
