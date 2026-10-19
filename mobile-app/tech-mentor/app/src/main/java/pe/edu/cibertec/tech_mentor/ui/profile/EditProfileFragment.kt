package pe.edu.cibertec.tech_mentor.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
import pe.edu.cibertec.tech_mentor.util.SessionManager
import java.util.Calendar

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var userApiService: UserApiService

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(binding.ivAvatarPreview)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userApiService = ApiClient.getClient(sessionManager).create(UserApiService::class.java)

        // --- Cargar inmediatamente datos locales en caché para que nunca aparezca en blanco: ---
        loadCachedData()

        // --- Conectar con el backend para sincronizar los datos más recientes: ---
        loadRemoteProfileData()

        setupDropdowns()
        setupListeners()
    }

    /** Carga inmediata desde SessionManager */
    private fun loadCachedData() {
        val nombres = sessionManager.getProfileNombres() ?: sessionManager.getUsername().orEmpty()
        val apellidos = sessionManager.getProfileApellidos().orEmpty()
        val telefono = sessionManager.getProfileTelefono().orEmpty()
        val pais = sessionManager.getProfilePais().orEmpty()
        val ciudad = sessionManager.getProfileCiudad().orEmpty()
        val biografia = sessionManager.getProfileBiografia().orEmpty()
        val fechaNac = sessionManager.getProfileFechaNac().orEmpty()
        val avatar = sessionManager.getProfileAvatar()

        if (nombres.isNotBlank()) binding.etNombres.setText(nombres)
        if (apellidos.isNotBlank()) binding.etApellidos.setText(apellidos)
        if (telefono.isNotBlank()) binding.etTelefono.setText(telefono)
        if (pais.isNotBlank()) binding.etPais.setText(pais)
        if (ciudad.isNotBlank()) binding.etCiudad.setText(ciudad)
        if (biografia.isNotBlank()) binding.etBiografia.setText(biografia)

        if (fechaNac.isNotBlank()) {
            // Formatear si viene en YYYY-MM-DD
            if (fechaNac.contains("-")) {
                val parts = fechaNac.split("-")
                if (parts.size == 3) {
                    binding.etFechaNac.setText("${parts[2]}/${parts[1]}/${parts[0]}")
                } else {
                    binding.etFechaNac.setText(fechaNac)
                }
            } else {
                binding.etFechaNac.setText(fechaNac)
            }
        }

        if (!avatar.isNullOrBlank()) {
            Glide.with(this)
                .load(avatar)
                .circleCrop()
                .into(binding.ivAvatarPreview)
        }
    }

    /** Carga desde el endpoint real /usuarios/me */
    private fun loadRemoteProfileData() {
        lifecycleScope.launch {
            try {
                val response = userApiService.getMyProfile()
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    val nombres = user.nombres.orEmpty()
                    val apellidos = user.apellidos.orEmpty()
                    val telefono = user.telefono.orEmpty()
                    val pais = user.pais.orEmpty()
                    val ciudad = user.ciudad.orEmpty()
                    val biografia = user.biografia.orEmpty()
                    val fechaNacRaw = user.fechaNacimiento.orEmpty()

                    if (nombres.isNotBlank()) binding.etNombres.setText(nombres)
                    if (apellidos.isNotBlank()) binding.etApellidos.setText(apellidos)
                    if (telefono.isNotBlank()) binding.etTelefono.setText(telefono)
                    if (pais.isNotBlank()) binding.etPais.setText(pais)
                    if (ciudad.isNotBlank()) binding.etCiudad.setText(ciudad)
                    if (biografia.isNotBlank()) binding.etBiografia.setText(biografia)

                    if (fechaNacRaw.isNotBlank() && fechaNacRaw.contains("-")) {
                        val parts = fechaNacRaw.split("-")
                        if (parts.size == 3) {
                            binding.etFechaNac.setText("${parts[2]}/${parts[1]}/${parts[0]}")
                        } else {
                            binding.etFechaNac.setText(fechaNacRaw)
                        }
                    } else if (fechaNacRaw.isNotBlank()) {
                        binding.etFechaNac.setText(fechaNacRaw)
                    }

                    val avatar = user.avatarUrl
                    if (!avatar.isNullOrBlank()) {
                        Glide.with(this@EditProfileFragment)
                            .load(avatar)
                            .circleCrop()
                            .into(binding.ivAvatarPreview)
                    }

                    // Guardar en sesión para futuras aperturas
                    sessionManager.saveUserProfile(
                        nombres = nombres,
                        apellidos = apellidos,
                        email = user.email,
                        telefono = telefono,
                        pais = pais,
                        ciudad = ciudad,
                        fechaNacimiento = fechaNacRaw,
                        biografia = biografia,
                        avatarUrl = avatar
                    )
                }
            } catch (e: Exception) {
                // Si la red falla, los datos en caché ya están cargados
            }
        }
    }

    private fun setupDropdowns() {
        val profesiones = listOf("Desarrollador Web", "Ingeniero de Software", "Diseñador UX/UI", "Data Scientist", "DevOps", "Desarrollador Móvil")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, profesiones)
        binding.actvProfesion.setAdapter(adapter)
    }

    private fun setupListeners() {
        // --- Botón Atrás Seguro (evita cierres forzosos usando NavController): ---
        binding.cvBack.setOnClickListener {
            navigateBackSafely()
        }

        binding.cvAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tilFechaNac.setEndIconOnClickListener {
            mostrarCalendario()
        }

        binding.etFechaNac.setOnClickListener {
            mostrarCalendario()
        }

        // --- Guardado real en backend (/usuarios/me): ---
        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun navigateBackSafely() {
        try {
            if (!findNavController().popBackStack()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        } catch (e: Exception) {
            requireActivity().finish()
        }
    }

    private fun saveProfile() {
        val nombres = binding.etNombres.text?.toString()?.trim().orEmpty()
        val apellidos = binding.etApellidos.text?.toString()?.trim().orEmpty()
        val telefono = binding.etTelefono.text?.toString()?.trim().orEmpty()
        val pais = binding.etPais.text?.toString()?.trim().orEmpty()
        val ciudad = binding.etCiudad.text?.toString()?.trim().orEmpty()
        val biografia = binding.etBiografia.text?.toString()?.trim().orEmpty()
        val fechaNacStr = binding.etFechaNac.text?.toString()?.trim().orEmpty()

        if (nombres.isBlank()) {
            binding.tilNombres.error = "Los nombres son requeridos"
            return
        }
        binding.tilNombres.error = null

        // Convertir fecha de DD/MM/AAAA a AAAA-MM-DD para el backend:
        var fechaBackend: String? = null
        if (fechaNacStr.isNotBlank() && fechaNacStr.contains("/")) {
            val parts = fechaNacStr.split("/")
            if (parts.size == 3 && parts[2].length == 4) {
                fechaBackend = "${parts[2]}-${parts[1].padStart(2, '0')}-${parts[0].padStart(2, '0')}"
            }
        } else if (fechaNacStr.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
            fechaBackend = fechaNacStr
        }

        val request = UpdateProfileRequestDto(
            nombres = nombres.ifBlank { null },
            apellidos = apellidos.ifBlank { null },
            fechaNacimiento = fechaBackend,
            telefono = telefono.ifBlank { null },
            pais = pais.ifBlank { null },
            ciudad = ciudad.ifBlank { null },
            biografia = biografia.ifBlank { null }
        )

        binding.btnSaveProfile.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = userApiService.updateMyProfile(request)
                binding.btnSaveProfile.isEnabled = true
                if (response.isSuccessful) {
                    sessionManager.saveUserProfile(
                        nombres = nombres,
                        apellidos = apellidos,
                        email = sessionManager.getProfileEmail(),
                        telefono = telefono,
                        pais = pais,
                        ciudad = ciudad,
                        fechaNacimiento = fechaBackend,
                        biografia = biografia
                    )
                    Toast.makeText(requireContext(), "¡Perfil actualizado con éxito!", Toast.LENGTH_SHORT).show()
                    navigateBackSafely()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar perfil (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.btnSaveProfile.isEnabled = true
                Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)
            binding.etFechaNac.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}