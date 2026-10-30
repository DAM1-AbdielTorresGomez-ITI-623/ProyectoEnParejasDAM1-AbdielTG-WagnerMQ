package cr.ac.utn.rota.domain.model

data class DonationRequest(
    val id: String,
    val itemId: String,
    val receiverId: String,
    val status: RequestStatus,
    val meetingPoint: MeetingPoint? = null
)
