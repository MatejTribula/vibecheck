package dk.sdu.vibecheck.data.repositories

import dk.sdu.vibecheck.data.local.storageRequest
import dk.sdu.vibecheck.data.remote.ApiClient
import dk.sdu.vibecheck.data.remote.LoginRequest
import dk.sdu.vibecheck.data.remote.RegisterRequest
import dk.sdu.vibecheck.domain.models.LoginSession
import dk.sdu.vibecheck.domain.models.Result
import dk.sdu.vibecheck.domain.models.User
import dk.sdu.vibecheck.domain.repositories.AuthRepository
import dk.sdu.vibecheck.domain.repositories.TokenStorage

class AuthRepositoryImpl(private val client: ApiClient, private val tokenStorage: TokenStorage) : AuthRepository {
    override suspend fun register(displayName: String, email: String, password: String): Result<User> {
        return client.request { client.api.register(RegisterRequest(displayName, email, password)) }
    }

    override suspend fun login(email: String, password: String, firebaseRegistrationId: String?): Result<LoginSession> {
        return storageRequest {
            val result = client.request { client.api.login(LoginRequest(email, password, firebaseRegistrationId)) }
            if (!result.hasError) {
                tokenStorage.saveToken(requireNotNull(result.value).token)
            }

            result
        }
    }
}
