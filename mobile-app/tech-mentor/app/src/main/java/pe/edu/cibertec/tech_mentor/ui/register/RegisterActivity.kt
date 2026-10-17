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
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.data.repository.FakeAuthRepository
import pe.edu.cibertec.tech_mentor.databinding.ActivityRegisterBinding
import pe.edu.cibertec.tech_mentor.domain.model.RegisterUserParams
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.ui.common.showAnimatedBit
import java.util.Calendar

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
        authRepository = FakeAuthRepository(sessionManager)

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

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
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

    private fun performRegister() {
        val params = RegisterUserParams(
            firstName = binding.etNombres.text?.toString()?.trim().orEmpty().ifEmpty { "Demo" },
            lastName = binding.etApellidos.text?.toString()?.trim().orEmpty().ifEmpty { "User" },
            email = binding.etEmail.text?.toString()?.trim().orEmpty().ifEmpty { "newuser@techmentor.com" },
            birthDate = binding.etFechaNac.text?.toString()?.trim().orEmpty(),
            phone = binding.etTelefono.text?.toString()?.trim().orEmpty(),
            country = binding.etPais.text?.toString()?.trim().orEmpty(),
            city = binding.etCiudad.text?.toString()?.trim().orEmpty(),
            biography = binding.etBiografia.text?.toString()?.trim().orEmpty(),
            avatar = selectedAvatarUri?.toString(),
            password = binding.etPassword.text?.toString()?.trim().orEmpty().ifEmpty { "123456" }
        )

        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            val result = authRepository.register(params)
            binding.btnRegister.isEnabled = true

            result.onSuccess {
                navigateToMain()
            }.onFailure { error ->
                Toast.makeText(this@RegisterActivity, error.message, Toast.LENGTH_SHORT).show()
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