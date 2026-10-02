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