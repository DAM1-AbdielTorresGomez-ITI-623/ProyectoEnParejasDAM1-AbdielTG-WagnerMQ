package cr.ac.utn.rota.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.rota.domain.model.DonationRequest
import cr.ac.utn.rota.domain.repository.IRequestRepository

class RequestViewModel(private val repository: IRequestRepository): ViewModel() {

    fun getRequests(): List<DonationRequest> {
        return repository.getRequests()
    }

    fun getRequestById(id: String): DonationRequest? {
        return repository.getRequestById(id)
    }

    fun createRequest(request: DonationRequest) {
        if (repository.getRequestById(request.id) != null)
            throw Exception("The request already exists.")
        repository.addRequest(request)
    }

    fun updateRequest(request: DonationRequest) {
        if (repository.getRequestById(request.id) == null)
            throw Exception("The request does not exist.")
        repository.updateRequest(request)
    }
}
