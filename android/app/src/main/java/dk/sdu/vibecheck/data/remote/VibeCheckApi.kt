package dk.sdu.vibecheck.data.remote

import dk.sdu.vibecheck.domain.models.LoginSession
import dk.sdu.vibecheck.domain.models.User
import dk.sdu.vibecheck.domain.models.UserSession
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface VibeCheckApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResult<User>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResult<LoginSession>>

    @POST("api/auth/forgot-password")
    suspend fun requestPasswordReset(@Body request: PasswordResetRequest): Response<ApiResult<PasswordResetResponse>>

    @GET("api/sessions/current")
    suspend fun validateToken(@Header("Authorization") authorization: String): Response<ApiResult<UserSession>>

    @PUT("api/sessions/current/firebase-registration")
    suspend fun updateFirebaseRegistration(@Header("Authorization") authorization: String, @Body request: Map<String, String>): Response<ApiResult<Unit>>

    @POST("api/sessions/logout")
    suspend fun logout(@Header("Authorization") authorization: String): Response<ApiResult<Unit>>
}
