package pe.edu.cibertec.tech_mentor.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.data.repository.FakeAuthRepository
import pe.edu.cibertec.tech_mentor.databinding.ActivityLoginBinding
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.ui.common.showAnimatedBit
import pe.edu.cibertec.tech_mentor.ui.forgotpassword.ForgotPasswordActivity
import pe.edu.cibertec.tech_mentor.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        authRepository = FakeAuthRepository(sessionManager)

        setupImage()
        setupAuthTabs()
        setupForgotPassword()
        setupListeners()
    }

    private fun setupImage() {
        showAnimatedBit(binding.ivRobotLogin)
    }

    private fun setupAuthTabs() {
        binding.authTabs.tvLogin.isSelected = true
        binding.authTabs.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }
    }

    private fun setupForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty().ifEmpty { "demo@techmentor.com" }
        val password = binding.etPassword.text?.toString()?.trim().orEmpty().ifEmpty { "123456" }

        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            val result = authRepository.login(email = email, password = password)
            binding.btnLogin.isEnabled = true

            result.onSuccess {
                navigateToMain()
            }.onFailure { error ->
                Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}