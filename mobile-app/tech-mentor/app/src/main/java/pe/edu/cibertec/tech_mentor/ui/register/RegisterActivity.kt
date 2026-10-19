package pe.edu.cibertec.tech_mentor.ui.register

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.databinding.ActivityRegisterBinding
import pe.edu.cibertec.tech_mentor.ui.common.showAnimatedBit
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.data.repository.AuthRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure
import java.util.Calendar

data class RegisterUserParams(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val phone: String,
    val country: String,
    val city: String,
    val biography: String,
    val avatar: String?,
    val password: String
)

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository
    private var selectedAvatarUri: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedAvatarUri = uri
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(binding.ivAvatarPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // --- Versión anterior simulada (comentada): ---
        // authRepository = FakeAuthRepository(sessionManager)

        // --- Versión anterior (comentada): ---
        // val authApiService = ApiClient.getClient(sessionManager).create(AuthApiService::class.java)
        // authRepository = AuthRepositoryImpl(authApiService, sessionManager)

        // --- Conexión con AuthApiService y UserApiService para soporte de registro: ---
        val retrofit = ApiClient.getClient(sessionManager)
        val authApiService = retrofit.create(AuthApiService::class.java)
        val userApiService = retrofit.create(UserApiService::class.java)
        authRepository = AuthRepositoryImpl(authApiService, sessionManager, userApiService)

        setupImage()
        setupDatePicker()
        setupPhotoPicker()
        setupListeners()
        setupDateFormatter()
    }

    private fun setupImage() {
        showAnimatedBit(binding.ivRobotRegister)
    }

    private fun setupDatePicker() {
        binding.tilFechaNac.setEndIconOnClickListener {
            showDatePickerDialog()
        }

        binding.etFechaNac.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // --- Versión anterior de fecha (comentada): ---
        // val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
        //     val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        //     binding.etFechaNac.setText(formattedDate)
        // }, year, month, day)

        // --- Versión mejorada con formato DD/MM/AAAA de 2 dígitos: ---
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            binding.etFechaNac.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun setupPhotoPicker() {
        binding.cvAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupListeners() {
        binding.authTabs.tvRegister.isSelected = true
        binding.authTabs.tvLogin.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun closeWithoutAnimation() {
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
    }

    // --- Versión anterior sin validación en cliente (comentada): ---
    // private fun performRegisterOld() {
    //     val params = RegisterUserParams(
    //         firstName = binding.etNombres.text?.toString()?.trim().orEmpty().ifEmpty { "Demo" },
    //         lastName = binding.etApellidos.text?.toString()?.trim().orEmpty().ifEmpty { "User" },
    //         email = binding.etEmail.text?.toString()?.trim().orEmpty().ifEmpty { "newuser@techmentor.com" },
    //         birthDate = binding.etFechaNac.text?.toString()?.trim().orEmpty(),
    //         phone = binding.etTelefono.text?.toString()?.trim().orEmpty(),
    //         country = binding.etPais.text?.toString()?.trim().orEmpty(),
    //         city = binding.etCiudad.text?.toString()?.trim().orEmpty(),
    //         biography = binding.etBiografia.text?.toString()?.trim().orEmpty(),
    //         avatar = selectedAvatarUri?.toString(),
    //         password = binding.etPassword.text?.toString()?.trim().orEmpty().ifEmpty { "123456" }
    //     )
    // }

    // --- Versión con validación completa según requisitos del backend (nombres, email, clave mín 8 caracteres): ---
    private fun performRegister() {
        // Limpiar errores visuales previos en los campos
        binding.tilNombres.error = null
        binding.tilApellidos.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        val firstName = binding.etNombres.text?.toString()?.trim().orEmpty()
        val lastName = binding.etApellidos.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
        val birthDate = binding.etFechaNac.text?.toString()?.trim().orEmpty()
        val phone = binding.etTelefono.text?.toString()?.trim().orEmpty()
        val country = binding.etPais.text?.toString()?.trim().orEmpty()
        val city = binding.etCiudad.text?.toString()?.trim().orEmpty()
        val biography = binding.etBiografia.text?.toString()?.trim().orEmpty()

        var isValid = true

        // 1. Validar Nombres (Obligatorio, máx 100 caracteres)
        if (firstName.isBlank()) {
            binding.tilNombres.error = "Ingresa tus nombres"
            isValid = false
        } else if (firstName.length > 100) {
            binding.tilNombres.error = "Máximo 100 caracteres"
            isValid = false
        }

        // 2. Validar Apellidos (Obligatorio, máx 100 caracteres)
        if (lastName.isBlank()) {
            binding.tilApellidos.error = "Ingresa tus apellidos"
            isValid = false
        } else if (lastName.length > 100) {
            binding.tilApellidos.error = "Máximo 100 caracteres"
            isValid = false
        }

        // 3. Validar Correo Electrónico (Obligatorio, formato email, máx 150 caracteres)
        if (email.isBlank()) {
            binding.tilEmail.error = "Ingresa tu correo electrónico"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Ingresa un correo electrónico válido (ej: usuario@correo.com)"
            isValid = false
        } else if (email.length > 150) {
            binding.tilEmail.error = "Máximo 150 caracteres"
            isValid = false
        }

        // 4. Validar Contraseña (Obligatorio según backend: Mínimo 8 caracteres, máx 100)
        if (password.isBlank()) {
            binding.tilPassword.error = "Ingresa una contraseña"
            isValid = false
        } else if (password.length < 8) {
            binding.tilPassword.error = "La contraseña debe tener al menos 8 caracteres"
            isValid = false
        } else if (password.length > 100) {
            binding.tilPassword.error = "Máximo 100 caracteres"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this@RegisterActivity, "Por favor completa los campos requeridos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val params = RegisterUserParams(
            firstName = firstName,
            lastName = lastName,
            email = email,
            birthDate = birthDate,
            phone = phone,
            country = country,
            city = city,
            biography = biography,
            avatar = selectedAvatarUri?.toString(),
            password = password
        )

        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            // --- Conexión real con el backend mediante authRepository.register(params): ---
            val result = authRepository.register(params)
            binding.btnRegister.isEnabled = true

            result.onSuccess {
                sessionManager.setOnboardingSeen()
                Toast.makeText(this@RegisterActivity, "¡Cuenta creada exitosamente! Bienvenido, ${params.firstName}", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }.onFailure { error ->
                val errorMsg = error.message.orEmpty()
                if (errorMsg.contains("correo", ignoreCase = true) || errorMsg.contains("email", ignoreCase = true)) {
                    binding.tilEmail.error = errorMsg
                }
                if (errorMsg.contains("contraseña", ignoreCase = true) || errorMsg.contains("password", ignoreCase = true)) {
                    binding.tilPassword.error = errorMsg
                }
                Toast.makeText(this@RegisterActivity, "Error al registrarse:\n$errorMsg", Toast.LENGTH_LONG).show()
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

    private fun setupDateFormatter() {
        binding.etFechaNac.addTextChangedListener(object : android.text.TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = java.util.Calendar.getInstance()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")

                    val cl = clean.length
                    var sel = cl
                    for (i in 2..cl step 2) {
                        sel++
                    }
                    if (clean == cleanC) sel--

                    if (clean.length < 8) {
                        clean += ddmmyyyy.substring(clean.length)
                    } else {
                        var day = Integer.parseInt(clean.substring(0, 2))
                        var mon = Integer.parseInt(clean.substring(2, 4))
                        var year = Integer.parseInt(clean.substring(4, 8))

                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                        cal.set(java.util.Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(java.util.Calendar.YEAR, year)

                        day = if (day > cal.getActualMaximum(java.util.Calendar.DATE)) cal.getActualMaximum(java.util.Calendar.DATE) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8))

                    sel = if (sel < 0) 0 else sel
                    current = clean
                    binding.etFechaNac.setText(current)
                    binding.etFechaNac.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }
}