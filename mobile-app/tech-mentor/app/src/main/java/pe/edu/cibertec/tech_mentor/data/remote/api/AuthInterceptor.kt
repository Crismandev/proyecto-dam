package pe.edu.cibertec.tech_mentor.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.cibertec.tech_mentor.data.local.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAuthToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}