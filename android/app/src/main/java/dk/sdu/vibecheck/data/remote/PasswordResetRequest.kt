package dk.sdu.vibecheck.data.remote

import com.google.gson.annotations.SerializedName

data class PasswordResetRequest(@SerializedName("email") val email: String)

data class PasswordResetResponse(@SerializedName("message") val message: String?)
