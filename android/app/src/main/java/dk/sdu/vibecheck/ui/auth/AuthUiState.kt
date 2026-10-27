package dk.sdu.vibecheck.ui.auth

import dk.sdu.vibecheck.domain.models.User

data class AuthUiState(val isLoading: Boolean = false, val isSessionChecked: Boolean = false, val userId: Long? = null, val user: User? = null, val registrationSuccessful: Boolean = false, val errorMessage: String? = null) {
    val isLoggedIn: Boolean
        get() = userId != null
}
