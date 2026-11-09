package dk.sdu.vibecheck.domain.models

enum class ErrorCode {
    None,
    InvalidRequest,
    Unauthorized,
    Forbidden,
    NotFound,
    DuplicateEntity,
    InternalError,
    NetworkError,
    StorageError
}
