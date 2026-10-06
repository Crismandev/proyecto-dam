package pe.edu.cibertec.tech_mentor.data.remote.api

import android.content.Context
import okhttp3.OkHttpClient
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Cambia esto por la IP de tu PC cuando pruebes con el celular
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    fun getAuthApi(context: Context): AuthApi {
        val sessionManager = SessionManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}