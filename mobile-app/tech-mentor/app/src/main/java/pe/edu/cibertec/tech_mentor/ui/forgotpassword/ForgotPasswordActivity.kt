package pe.edu.cibertec.tech_mentor.ui.forgotpassword

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
// Asegúrate de que el import del binding coincida con el nombre de tu paquete
import pe.edu.cibertec.tech_mentor.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupImage()
        setupListeners()
    }

    private fun setupImage() {
        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjNDbDyBz9vuv0SOM3tfDx6fZVbUZJgF10nUXLmdT6hxpAgWd0kDlSSwc&s=10")
            .into(binding.ivRobotForgotPassword)
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            finish()
        }
    }
}