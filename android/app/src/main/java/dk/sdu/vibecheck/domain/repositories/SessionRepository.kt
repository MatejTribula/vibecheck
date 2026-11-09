package dk.sdu.vibecheck.domain.repositories

import dk.sdu.vibecheck.domain.models.Result
import dk.sdu.vibecheck.domain.models.UserSession

interface SessionRepository {
    suspend fun validateToken(token: String? = null): Result<UserSession>

    suspend fun logout(token: String? = null): Result<Unit>
}
