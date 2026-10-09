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

    // 1. Configuramos el launcher del Photo Picker (Selector de galería sin permisos)
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            // Si el usuario seleccionó una imagen, la cargamos con Glide y la hacemos circular
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(binding.ivAvatarPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos ViewBinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Llamamos a nuestros métodos de configuración
        setupImage()
        setupDatePicker()
        setupPhotoPicker()
        setupListeners()
    }

    private fun setupImage() {
        // Cargar imagen de un robot aleatorio (banner) usando Glide, similar al Login
        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjNDbDyBz9vuv0SOM3tfDx6fZVbUZJgF10nUXLmdT6hxpAgWd0kDlSSwc&s=10")
            .into(binding.ivRobotRegister)
    }

    private fun setupDatePicker() {
        // Al presionar el ícono del calendario al final del input
        binding.tilFechaNac.setEndIconOnClickListener {
            mostrarCalendario()
        }

        // Opcional: También abrir el calendario si el usuario toca el texto directamente
        binding.etFechaNac.setOnClickListener {
            mostrarCalendario()
        }
    }

    private fun mostrarCalendario() {
        // Obtenemos la fecha actual para que el calendario inicie allí
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        // Creamos y mostramos el DatePickerDialog de Android
        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            // Formateamos la fecha seleccionada y la colocamos en el input
            val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
            binding.etFechaNac.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    private fun setupPhotoPicker() {
        // Al presionar la tarjeta circular del avatar
        binding.cvAvatar.setOnClickListener {
            // Lanzamos el Photo Picker pidiendo que muestre solo imágenes
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupListeners() {
        // Botón atrás para cerrar esta pantalla
        binding.cvBack.setOnClickListener {
            finish()
        }

        // Lógica futura para el botón registrar
        binding.btnRegister.setOnClickListener {
            // Aquí irá tu lógica para recolectar los datos y enviarlos al backend
        }
    }
}