package pe.edu.cibertec.tech_mentor.data.remote

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>()
    data class Exception(val e: Throwable) : ApiResponse<Nothing>()
}