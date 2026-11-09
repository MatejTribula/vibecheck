package dk.sdu.vibecheck.data.repositories

import dk.sdu.vibecheck.data.local.storageRequest
import dk.sdu.vibecheck.data.remote.ApiClient
import dk.sdu.vibecheck.domain.models.ErrorCode
import dk.sdu.vibecheck.domain.models.Result
import dk.sdu.vibecheck.domain.models.UserSession
import dk.sdu.vibecheck.domain.repositories.SessionRepository
import dk.sdu.vibecheck.domain.repositories.TokenStorage

class SessionRepositoryImpl(private val client: ApiClient, private val tokenStorage: TokenStorage) : SessionRepository {
    override suspend fun validateToken(token: String?): Result<UserSession> {
        return withToken(token) { savedToken ->
            client.request { client.api.validateToken("Bearer $savedToken") }
        }
    }

    override suspend fun logout(token: String?): Result<Unit> {
        return withToken(token, clearOnSuccess = true) { savedToken ->
            client.request(allowEmptyValue = true) { client.api.logout("Bearer $savedToken") }
        }
    }

    private suspend fun <T> withToken(token: String?, clearOnSuccess: Boolean = false, call: suspend (String) -> Result<T>): Result<T> {
        return storageRequest {
            val savedToken = token ?: tokenStorage.getToken()
            if (savedToken == null) {
                return@storageRequest Result(errorCode = ErrorCode.Unauthorized, message = "No saved login found.")
            }

            val result = call(savedToken)
            if ((clearOnSuccess && !result.hasError) || result.errorCode == ErrorCode.Unauthorized) {
                tokenStorage.clearToken(savedToken)
            }

            result
        }
    }
}
