package pe.edu.cibertec.tech_mentor.data.remote

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>()
    data class Exception(val e: Throwable) : ApiResponse<Nothing>()
}

val <T> ApiResponse<T>.isSuccess: Boolean
    get() = this is ApiResponse.Success

fun <T> ApiResponse<T>.getOrDefault(defaultValue: T): T {
    return when (this) {
        is ApiResponse.Success -> this.data
        else -> defaultValue
    }
}

fun <T> ApiResponse<T>.getOrNull(): T? {
    return when (this) {
        is ApiResponse.Success -> this.data
        else -> null
    }
}

inline fun <T> ApiResponse<T>.onSuccess(action: (value: T) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        action(this.data)
    }
    return this
}

inline fun <T> ApiResponse<T>.onFailure(action: (exception: Throwable) -> Unit): ApiResponse<T> {
    when (this) {
        is ApiResponse.Error -> action(kotlin.Exception(this.message))
        is ApiResponse.Exception -> action(this.e)
        else -> Unit
    }
    return this
}