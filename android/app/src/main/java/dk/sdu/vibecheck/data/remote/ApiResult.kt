package dk.sdu.vibecheck.data.remote

data class ApiResult<T>(val value: T?, val errorCode: String?, val message: String?, val hasError: Boolean?)
