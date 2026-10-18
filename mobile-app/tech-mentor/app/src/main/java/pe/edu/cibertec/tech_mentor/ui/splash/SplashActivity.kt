package pe.edu.cibertec.tech_mentor.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity
import pe.edu.cibertec.tech_mentor.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        val destination = when {
            sessionManager.isUserLoggedIn() -> MainActivity::class.java
            sessionManager.isOnboardingSeen() -> LoginActivity::class.java
            else -> OnboardingActivity::class.java
        }
        startActivity(Intent(this, destination))
        finish()
    }
}