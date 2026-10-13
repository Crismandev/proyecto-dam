package pe.edu.cibertec.tech_mentor.ui.login

import android.content.Intent
// import android.graphics.Color // Usado en la versión anterior.
import android.os.Bundle
// import android.text.SpannableString // Usado en la versión anterior.
// import android.text.Spanned // Usado en la versión anterior.
// import android.text.TextPaint // Usado en la versión anterior.
// import android.text.method.LinkMovementMethod // Usado en la versión anterior.
// import android.text.style.ClickableSpan // Usado en la versión anterior.
// import android.view.View // Usado en la versión anterior.
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.tech_mentor.ui.common.showAnimatedBit
// import com.bumptech.glide.Glide // Usado en la versión anterior.
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

        binding.btnLogin.setOnClickListener {
            sessionManager.saveAuthToken("token_virtual_12345")

            startActivity(Intent(this, MainActivity::class.java))
        }

        setupImage()

        // setupTextViewColors() // El enlace anterior se reemplazó por las pestañas.
        setupAuthTabs()

        setupForgotPassword()
    }

// Versión anterior conservada; sustituida por el bloque activo que sigue.
//     private fun setupImage() {
//         Glide.with(this)
//             .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjNDbDyBz9vuv0SOM3tfDx6fZVbUZJgF10nUXLmdT6hxpAgWd0kDlSSwc&s=10")
//             .into(binding.ivRobotLogin)
//     }

    private fun setupImage() {
        showAnimatedBit(binding.ivRobotLogin)
    }

// Versión anterior conservada; sustituida por el bloque activo que sigue.
//     private fun setupTextViewColors() {
//         val textoCompleto = "¿Es tu primera vez? Crea tu cuenta"
//         val spannableString = SpannableString(textoCompleto)
//
//         val startIndex = textoCompleto.indexOf("Crea tu cuenta")
//         val endIndex = startIndex + "Crea tu cuenta".length
//
//         val clickableSpan = object : ClickableSpan() {
//             override fun onClick(widget: View) {
//                 startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//             }
//
//             override fun updateDrawState(ds: TextPaint) {
//                 super.updateDrawState(ds)
//                 ds.isUnderlineText = false
//                 ds.color = Color.parseColor("#7C4DFF")
//             }
//         }
//
//         spannableString.setSpan(
//             clickableSpan,
//             startIndex,
//             endIndex,
//             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//         )
//
//         binding.tvRegister.text = spannableString
//         binding.tvRegister.movementMethod = LinkMovementMethod.getInstance()
//         binding.tvRegister.highlightColor = Color.TRANSPARENT
//     }

    private fun setupAuthTabs() {
        binding.authTabs.tvLogin.isSelected = true
        binding.authTabs.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}