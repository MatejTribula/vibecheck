package dk.sdu.vibecheck.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dk.sdu.vibecheck.domain.models.ErrorCode
import dk.sdu.vibecheck.domain.models.Result
import dk.sdu.vibecheck.domain.repositories.AuthRepository
import dk.sdu.vibecheck.domain.repositories.SessionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository, private val sessionRepository: SessionRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState(isLoading = true))
    private var currentJob: Job? = null

    val uiState = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun login(email: String, password: String, firebaseRegistrationId: String? = null) {
        execute {
            val result = authRepository.login(email, password, firebaseRegistrationId)
            if (result.hasError) {
                showError(result)

                return@execute
            }

            val user = requireNotNull(result.value).user
            _uiState.update { it.copy(isSessionChecked = true, userId = user.id, user = user) }
        }
    }

    fun register(displayName: String, email: String, password: String) {
        execute {
            val result = authRepository.register(displayName, email, password)
            if (result.hasError) {
                showError(result)

                return@execute
            }

            _uiState.update { it.copy(registrationSuccessful = true) }
        }
    }

    fun checkSession() {
        execute {
            val result = sessionRepository.validateToken()
            if (result.errorCode == ErrorCode.Unauthorized) {
                _uiState.value = AuthUiState(isLoading = true, isSessionChecked = true)

                return@execute
            }

            if (result.hasError) {
                showError(result)

                return@execute
            }

            val session = requireNotNull(result.value)
            _uiState.update { it.copy(isSessionChecked = true, userId = session.userId, user = it.user?.takeIf { user -> user.id == session.userId }) }
        }
    }

    fun logout() {
        execute {
            val result = sessionRepository.logout()
            if (result.hasError && result.errorCode != ErrorCode.Unauthorized) {
                showError(result)

                return@execute
            }

            _uiState.value = AuthUiState(isLoading = true, isSessionChecked = true)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, registrationSuccessful = false) }
    }

    private fun execute(action: suspend () -> Unit) {
        if (currentJob?.isActive == true) {
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, registrationSuccessful = false) }

        currentJob = viewModelScope.launch {
            try {
                action()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { it.copy(errorMessage = "Something went wrong. Please try again.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun showError(result: Result<*>) {
        _uiState.update { it.copy(errorMessage = result.message ?: "Something went wrong. Please try again.") }
    }

    companion object {
        fun factory(authRepository: AuthRepository, sessionRepository: SessionRepository): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AuthViewModel(authRepository, sessionRepository)
                }
            }
        }
    }
}
