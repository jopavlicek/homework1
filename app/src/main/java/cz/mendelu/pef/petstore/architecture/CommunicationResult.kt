package cz.mendelu.pef.petstore.architecture

sealed class CommunicationResult<out T: Any> {
    class Success<T: Any>(val data: T) : CommunicationResult<T>()
    class Error(val error: cz.mendelu.pef.petstore.architecture.Error)
        : CommunicationResult<Nothing>()
    class CommunicationError(): CommunicationResult<Nothing>()
    class Exception(val exception: Throwable) : CommunicationResult<Nothing>()
}