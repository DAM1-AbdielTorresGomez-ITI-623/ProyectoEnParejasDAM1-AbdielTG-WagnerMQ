package cr.ac.utn.rota.domain.repository

import cr.ac.utn.rota.domain.model.DonationRequest

interface IRequestRepository {
    fun getRequests(): List<DonationRequest>
    fun getRequestById(id: String): DonationRequest?
    fun addRequest(request: DonationRequest)
    fun updateRequest(request: DonationRequest)
}
