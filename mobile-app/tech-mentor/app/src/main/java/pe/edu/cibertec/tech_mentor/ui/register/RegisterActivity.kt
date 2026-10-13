package pe.edu.cibertec.tech_mentor.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pe.edu.cibertec.tech_mentor.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
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

        setupImage()
        setupDatePicker()
        setupPhotoPicker()
        setupListeners()
    }

    private fun setupImage() {
        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjNDbDyBz9vuv0SOM3tfDx6fZVbUZJgF10nUXLmdT6hxpAgWd0kDlSSwc&s=10")
            .into(binding.ivRobotRegister)
    }

    private fun setupDatePicker() {
        binding.tilFechaNac.setEndIconOnClickListener {
            mostrarCalendario()
        }

        binding.etFechaNac.setOnClickListener {
            mostrarCalendario()
        }
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
            binding.etFechaNac.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    private fun setupPhotoPicker() {
        binding.cvAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {

        }
    }
}