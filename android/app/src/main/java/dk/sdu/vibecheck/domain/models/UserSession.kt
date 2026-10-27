package dk.sdu.vibecheck.domain.models

data class UserSession(val id: Long, val userId: Long, val expiresAt: String)
