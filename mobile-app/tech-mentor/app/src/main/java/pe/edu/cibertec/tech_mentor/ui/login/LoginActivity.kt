package pe.edu.cibertec.tech_mentor.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
// Faltaba: import para detectar cambios de tamaño del viewport (teclado visible/oculto)
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.databinding.ActivityLoginBinding
import pe.edu.cibertec.tech_mentor.ui.common.showAnimatedBit
import pe.edu.cibertec.tech_mentor.ui.forgotpassword.ForgotPasswordActivity
import pe.edu.cibertec.tech_mentor.ui.register.RegisterActivity
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.data.repository.AuthRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApiService
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // --- Versión anterior simulada (comentada): ---
        // authRepository = FakeAuthRepository(sessionManager)

        // --- Conexión real con el backend mediante Retrofit: ---
        val authApiService = ApiClient.getClient(sessionManager).create(AuthApiService::class.java)
        authRepository = AuthRepositoryImpl(authApiService, sessionManager)

        setupImage()
        setupAuthTabs()
        setupForgotPassword()
        setupListeners()
        // Faltaba: detectar teclado para ocultar la imagen del robot y liberar espacio
        setupKeyboardVisibilityListener()
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

    // Faltaba: ocultar la imagen del robot cuando el teclado aparece para que los campos queden visibles
    private fun setupKeyboardVisibilityListener() {
        // Versión anterior (comentada): no había listener — la imagen ocupaba espacio y tapaba los campos
        // (no existía ningún mecanismo para ocultar ivRobotLogin al abrir el teclado)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            // Faltaba: ocultar imagen cuando el teclado está abierto y mostrarla cuando se cierra
            binding.ivRobotLogin.visibility = if (imeVisible) View.GONE else View.VISIBLE
            insets
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty().ifEmpty { "demo@techmentor.com" }
        val password = binding.etPassword.text?.toString()?.trim().orEmpty().ifEmpty { "123456" }

        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val result = authRepository.login(username = email, password = password)
                result.onSuccess {
                    navigateToMain()
                }.onFailure { error ->
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            } catch (error: Exception) {
                Toast.makeText(this@LoginActivity, error.message ?: "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnLogin.isEnabled = true
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
