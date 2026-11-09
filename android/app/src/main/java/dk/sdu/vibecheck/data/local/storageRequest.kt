package dk.sdu.vibecheck.data.local

import dk.sdu.vibecheck.domain.models.ErrorCode
import dk.sdu.vibecheck.domain.models.Result
import java.io.IOException
import java.security.GeneralSecurityException

suspend fun <T> storageRequest(call: suspend () -> Result<T>): Result<T> {
    try {
        return call()
    } catch (error: IOException) {
        return Result(errorCode = ErrorCode.StorageError, message = "Could not access saved login. Please try again.")
    } catch (error: GeneralSecurityException) {
        return Result(errorCode = ErrorCode.StorageError, message = "Could not access saved login. Please try again.")
    }
}
