package dk.sdu.vibecheck.domain.models

data class LoginSession(val user: User, val token: String, val expiresAt: String)
