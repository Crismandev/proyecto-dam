package pe.edu.cibertec.tech_mentor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.databinding.ActivityMainBinding
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnCerrarSesion.setOnClickListener {
            sessionManager.clearSession()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}