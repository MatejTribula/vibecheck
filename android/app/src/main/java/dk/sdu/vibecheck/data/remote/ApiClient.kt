package dk.sdu.vibecheck.data.remote

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import dk.sdu.vibecheck.domain.models.ErrorCode
import dk.sdu.vibecheck.domain.models.Result
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.EOFException
import java.util.concurrent.TimeUnit

class ApiClient(baseUrl: String) {
    private val gson = Gson()
    private val httpClient = OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build()

    val api: VibeCheckApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(VibeCheckApi::class.java)

    suspend fun <T> request(allowEmptyValue: Boolean = false, call: suspend () -> Response<ApiResult<T>>): Result<T> {
        try {
            val response = call()
            if (!response.isSuccessful) {
                val error = response.errorBody()?.use { gson.fromJson(it.charStream(), ApiResult::class.java) }
                val code = ErrorCode.entries.find { it.name == error?.errorCode && it != ErrorCode.None } ?: httpErrorCode(response.code())

                return Result(errorCode = code, message = error?.message ?: "Request failed.")
            }

            val body = response.body()
            if (body == null || body.hasError == null || body.errorCode == null) {
                return Result(errorCode = ErrorCode.InternalError, message = "Invalid server response.")
            }

            val code = ErrorCode.entries.find { it.name == body.errorCode } ?: ErrorCode.InternalError
            if (body.hasError || code != ErrorCode.None) {
                return Result(errorCode = if (code == ErrorCode.None) ErrorCode.InternalError else code, message = body.message ?: "Request failed.")
            }

            if (body.value == null && !allowEmptyValue) {
                return Result(errorCode = ErrorCode.InternalError, message = "Invalid server response.")
            }

            return Result(value = body.value)
        } catch (error: JsonParseException) {
            return Result(errorCode = ErrorCode.InternalError, message = "Invalid server response.")
        } catch (error: IOException) {
            if (error is MalformedJsonException || error is EOFException) {
                return Result(errorCode = ErrorCode.InternalError, message = "Invalid server response.")
            }

            return Result(errorCode = ErrorCode.NetworkError, message = "Could not connect to the server. Please try again.")
        }
    }

    private fun httpErrorCode(status: Int): ErrorCode {
        return when (status) {
            400 -> ErrorCode.InvalidRequest
            401 -> ErrorCode.Unauthorized
            403 -> ErrorCode.Forbidden
            404 -> ErrorCode.NotFound
            409 -> ErrorCode.DuplicateEntity
            else -> ErrorCode.InternalError
        }
    }
}
