
package cr.ac.utn.rota

import cr.ac.utn.rota.data.memory.MemoryDataRepository
import cr.ac.utn.rota.domain.repository.IRotaRepository

object RotaData {
    val repository: IRotaRepository = MemoryDataRepository()
}
