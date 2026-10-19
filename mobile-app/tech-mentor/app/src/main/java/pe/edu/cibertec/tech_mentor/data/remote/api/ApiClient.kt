package pe.edu.cibertec.tech_mentor.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.edu.cibertec.tech_mentor.util.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // =========================================================================
    // CONFIGURACIÓN GLOBAL DE URL BASE (Retrofit)
    // =========================================================================
    // Opción 1: Emulador Android (conectando al backend local de tu PC) [ACTIVA]
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Opción 2: Celular Físico en la misma red Wi-Fi (reemplaza XX por la IP de tu PC):
    // private const val BASE_URL = "http://192.168.1.XX:8080/"

    // Opción 3: Backend en Producción / Cloud (Render, Cloud Run, Railway, etc.):
    // private const val BASE_URL = "https://tu-backend-api.onrender.com/"
    // =========================================================================

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