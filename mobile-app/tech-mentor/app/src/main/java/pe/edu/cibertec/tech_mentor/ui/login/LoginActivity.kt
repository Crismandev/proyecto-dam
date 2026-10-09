package pe.edu.cibertec.tech_mentor.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.databinding.ActivityLoginBinding
import pe.edu.cibertec.tech_mentor.ui.register.RegisterActivity
// Importamos tu nueva actividad
import pe.edu.cibertec.tech_mentor.ui.forgotpassword.ForgotPasswordActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // 1. Lógica del botón con inicio de sesión falso conectado a btnLogin
        binding.btnLogin.setOnClickListener {
            sessionManager.saveAuthToken("token_virtual_12345")

            startActivity(Intent(this, MainActivity::class.java))
        }

        // 2. Cargar imagen random con Glide
        setupImage()

        // 3. Configurar el texto de registro
        setupTextViewColors()

        // 4. Configurar la navegación hacia la recuperación de contraseña
        setupForgotPassword()
    }

    private fun setupImage() {
        // Usamos una URL de RoboHash que genera un robot aleatorio (ideal para "Bit")
        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjNDbDyBz9vuv0SOM3tfDx6fZVbUZJgF10nUXLmdT6hxpAgWd0kDlSSwc&s=10")
            .into(binding.ivRobotLogin)
    }

    private fun setupTextViewColors() {
        val textoCompleto = "¿Es tu primera vez? Crea tu cuenta"
        val spannableString = SpannableString(textoCompleto)

        val startIndex = textoCompleto.indexOf("Crea tu cuenta")
        val endIndex = startIndex + "Crea tu cuenta".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navegación hacia RegisterActivity
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                // Color morado de tu línea gráfica
                ds.color = Color.parseColor("#7C4DFF")
            }
        }

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvRegister.text = spannableString
        binding.tvRegister.movementMethod = LinkMovementMethod.getInstance()
        binding.tvRegister.highlightColor = Color.TRANSPARENT
    }

    // Nuevo método para manejar el clic en "¿Olvidaste tu contraseña?"
    private fun setupForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}