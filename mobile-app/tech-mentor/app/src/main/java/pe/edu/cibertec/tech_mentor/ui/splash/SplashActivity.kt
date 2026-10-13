package pe.edu.cibertec.tech_mentor.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity
import pe.edu.cibertec.tech_mentor.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        val destination = when {
            sessionManager.isUserLoggedIn() -> MainActivity::class.java
            sessionManager.isOnboardingVisto() -> LoginActivity::class.java
            else -> OnboardingActivity::class.java
        }
        startActivity(Intent(this, destination))
        finish()
    }
}

/*
 * Versión anterior conservada por solicitud de la usuaria.
 * La versión activa mantiene la prioridad de sesión y consulta si la bienvenida ya fue vista antes de abrir Login.
 * El código siguiente permanece únicamente como referencia y no se ejecuta.

package pe.edu.cibertec.tech_mentor.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)

        if (sessionManager.isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }

        finish()
    }
}
*/
