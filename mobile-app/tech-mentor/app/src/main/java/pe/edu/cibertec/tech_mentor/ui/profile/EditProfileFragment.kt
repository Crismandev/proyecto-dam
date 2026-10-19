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
<<<<<<< HEAD
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
=======
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
import pe.edu.cibertec.tech_mentor.util.AvatarUrls
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021
import pe.edu.cibertec.tech_mentor.util.SessionManager
import java.util.Calendar
import kotlin.random.Random

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

<<<<<<< HEAD
    private lateinit var sessionManager: SessionManager
    private lateinit var userApiService: UserApiService
=======
    private val vm: ProfileViewModel by activityViewModels {
        ProfileViewModel.Factory(requireContext().applicationContext)
    }

    private var currentAvatarUrl: String? = null
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            currentAvatarUrl = uri.toString()
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

<<<<<<< HEAD
        sessionManager = SessionManager(requireContext())
        userApiService = ApiClient.getClient(sessionManager).create(UserApiService::class.java)

        // --- Cargar inmediatamente datos locales en caché para que nunca aparezca en blanco: ---
        loadCachedData()

        // --- Conectar con el backend para sincronizar los datos más recientes: ---
        loadRemoteProfileData()

=======
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021
        setupDropdowns()
        setupListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { bindProfile(it) }
            }
        }
        vm.refresh()
    }

<<<<<<< HEAD
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
=======
    private fun bindProfile(state: ProfileUiState) {
        val user = state.user ?: return
        if (binding.etNombres.text.isNullOrBlank()) {
            binding.etNombres.setText(user.nombres)
            binding.etApellidos.setText(user.apellidos)
            binding.etFechaNac.setText(toDisplayDate(user.fechaNacimiento))
            binding.etTelefono.setText(user.telefono.orEmpty())
            binding.etPais.setText(user.pais.orEmpty())
            binding.etCiudad.setText(user.ciudad.orEmpty())
            binding.etBiografia.setText(user.biografia.orEmpty())
            binding.actvProfesion.setText(state.profession.orEmpty(), false)
        }
        currentAvatarUrl = user.avatarUrl
        val seed = user.nombres.ifBlank { SessionManager(requireContext()).getUsername() ?: "techmentor" }
        Glide.with(this)
            .load(AvatarUrls.of(seed, user.avatarUrl))
            .circleCrop()
            .placeholder(R.drawable.ic_user)
            .into(binding.ivAvatarPreview)
    }

    private fun setupDropdowns() {
        val profesiones = listOf(
            "Desarrollador Web",
            "Ingeniero de Software",
            "Diseñador UX/UI",
            "Data Scientist",
            "DevOps"
        )
        binding.actvProfesion.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, profesiones)
        )
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021
    }

    private fun setupListeners() {
        // --- Botón Atrás Seguro (evita cierres forzosos usando NavController): ---
        binding.cvBack.setOnClickListener {
            navigateBackSafely()
        }

        binding.cvAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tvCambiarFoto.setOnClickListener {
            val seed = (SessionManager(requireContext()).getUsername() ?: "techmentor") + "-" + Random.nextInt(100_000)
            currentAvatarUrl = AvatarUrls.random(seed)
            Glide.with(this)
                .load(currentAvatarUrl)
                .circleCrop()
                .into(binding.ivAvatarPreview)
        }

        binding.tilFechaNac.setEndIconOnClickListener { mostrarCalendario() }
        binding.etFechaNac.setOnClickListener { mostrarCalendario() }

        // --- Guardado real en backend (/usuarios/me): ---
        binding.btnSaveProfile.setOnClickListener {
<<<<<<< HEAD
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
=======
            vm.updateProfile(
                UpdateProfileRequestDto(
                    nombres = binding.etNombres.text?.toString()?.trim(),
                    apellidos = binding.etApellidos.text?.toString()?.trim(),
                    fechaNacimiento = toIsoDate(binding.etFechaNac.text?.toString()),
                    avatarUrl = currentAvatarUrl,
                    biografia = binding.etBiografia.text?.toString()?.trim(),
                    telefono = binding.etTelefono.text?.toString()?.trim(),
                    pais = binding.etPais.text?.toString()?.trim(),
                    ciudad = binding.etCiudad.text?.toString()?.trim()
                )
            ) { ok, error ->
                if (!isAdded) return@updateProfile
                if (ok) {
                    Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                } else {
                    Toast.makeText(requireContext(), error ?: "No se pudo guardar", Toast.LENGTH_SHORT).show()
                }
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021
            }
        }
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
<<<<<<< HEAD
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)
            binding.etFechaNac.setText(fechaSeleccionada)
        }, anio, mes, dia)

=======
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                binding.etFechaNac.setText(
                    String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)
                )
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
>>>>>>> 63224903ebb83f355b914bb667ff37c818b59021
        datePickerDialog.show()
    }

    private fun toDisplayDate(iso: String?): String {
        if (iso.isNullOrBlank()) return ""
        val parts = iso.split("-")
        return if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else iso
    }

    private fun toIsoDate(display: String?): String? {
        val value = display?.trim().orEmpty()
        if (value.isEmpty()) return null
        val parts = value.split("/", "-")
        if (parts.size == 3 && parts[0].length <= 2) {
            return "${parts[2]}-${parts[1].padStart(2, '0')}-${parts[0].padStart(2, '0')}"
        }
        return value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
