package pe.edu.cibertec.tech_mentor.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.cibertec.tech_mentor.util.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = sessionManager.getToken()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}