package pe.edu.cibertec.tech_mentor.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnIniciarSesionFalso.setOnClickListener {
            sessionManager.saveAuthToken("token_virtual_12345")

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}