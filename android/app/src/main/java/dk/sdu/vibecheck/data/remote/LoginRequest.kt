package dk.sdu.vibecheck.data.remote

data class LoginRequest(val email: String, val password: String, val firebaseRegistrationId: String? = null)
