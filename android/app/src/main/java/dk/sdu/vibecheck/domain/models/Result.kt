package dk.sdu.vibecheck.domain.models

data class Result<out T>(val value: T? = null, val errorCode: ErrorCode = ErrorCode.None, val message: String? = null) {
    val hasError: Boolean
        get() = errorCode != ErrorCode.None
}
