package dk.sdu.vibecheck.data.remote

import dk.sdu.vibecheck.domain.models.LoginSession
import dk.sdu.vibecheck.domain.models.User
import dk.sdu.vibecheck.domain.models.UserSession
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface VibeCheckApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResult<User>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResult<LoginSession>>

    @GET("api/sessions/current")
    suspend fun validateToken(@Header("Authorization") authorization: String): Response<ApiResult<UserSession>>

    @POST("api/sessions/logout")
    suspend fun logout(@Header("Authorization") authorization: String): Response<ApiResult<Unit>>
}
