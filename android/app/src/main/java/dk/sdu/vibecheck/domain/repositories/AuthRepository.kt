package dk.sdu.vibecheck.domain.repositories

import dk.sdu.vibecheck.domain.models.LoginSession
import dk.sdu.vibecheck.domain.models.Result
import dk.sdu.vibecheck.domain.models.User

interface AuthRepository {
    suspend fun register(displayName: String, email: String, password: String): Result<User>

    suspend fun login(email: String, password: String, firebaseRegistrationId: String? = null): Result<LoginSession>
}
