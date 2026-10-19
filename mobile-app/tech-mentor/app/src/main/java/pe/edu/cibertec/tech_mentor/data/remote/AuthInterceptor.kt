package pe.edu.cibertec.tech_mentor.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.cibertec.tech_mentor.util.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // --- Versión anterior (comentada): ---
        // val requestBuilder = chain.request().newBuilder()
        // val token = sessionManager.getToken()
        // if (!token.isNullOrEmpty()) {
        //     requestBuilder.addHeader("Authorization", "Bearer $token")
        // }
        // return chain.proceed(requestBuilder.build())

        // --- Versión adaptada: respeta cabeceras Authorization explícitas (p. ej. token administrativo de registro) ---
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (originalRequest.header("Authorization") == null) {
            val token = sessionManager.getToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}