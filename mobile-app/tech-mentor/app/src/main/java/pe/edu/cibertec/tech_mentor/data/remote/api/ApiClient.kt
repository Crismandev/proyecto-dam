package pe.edu.cibertec.tech_mentor.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.edu.cibertec.tech_mentor.util.ApiConfig
import pe.edu.cibertec.tech_mentor.util.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // No pongas la IP aquí. Va en local.properties (api.ip) + Sync Gradle.
    // No envíes solo este archivo: hay que hacer git pull de todo el repo.
    private val BASE_URL: String = ApiConfig.baseUrl()

    fun getClient(sessionManager: SessionManager): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}