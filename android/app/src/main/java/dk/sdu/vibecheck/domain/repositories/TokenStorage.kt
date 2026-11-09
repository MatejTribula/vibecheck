package dk.sdu.vibecheck.domain.repositories

interface TokenStorage {
    suspend fun saveToken(token: String)

    suspend fun getToken(): String?

    suspend fun clearToken(expectedToken: String? = null)
}
