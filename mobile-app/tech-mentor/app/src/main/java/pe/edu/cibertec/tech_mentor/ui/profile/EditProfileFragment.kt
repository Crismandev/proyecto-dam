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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
import pe.edu.cibertec.tech_mentor.util.AvatarUrls
import pe.edu.cibertec.tech_mentor.util.SessionManager
import java.util.Calendar
import kotlin.random.Random

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var userApiService: UserApiService

    private val vm: ProfileViewModel by activityViewModels {
        ProfileViewModel.Factory(requireContext().applicationContext)
    }

    private var currentAvatarUrl: String? = null

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

        sessionManager = SessionManager(requireContext())
        userApiService = ApiClient.getClient(sessionManager).create(UserApiService::class.java)

        // 1. Cargar inmediatamente desde SessionManager para que nunca aparezca vacío
        loadCachedData()

        setupDropdowns()
        setupListeners()

        // 2. Observar los datos reactivos del ViewModel y refrescar desde el backend
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { bindProfile(it) }
            }
        }
        vm.refresh()
    }

    /** Carga instantánea desde SessionManager */
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
            binding.etFechaNac.setText(toDisplayDate(fechaNac))
        }

        if (!avatar.isNullOrBlank()) {
            currentAvatarUrl = avatar
            Glide.with(this)
                .load(avatar)
                .circleCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivAvatarPreview)
        }
    }

    /** Vincula los datos del ProfileViewModel cuando el backend responde */
    private fun bindProfile(state: ProfileUiState) {
        val user = state.user ?: return

        binding.etNombres.setText(user.nombres)
        binding.etApellidos.setText(user.apellidos)
        binding.etFechaNac.setText(toDisplayDate(user.fechaNacimiento))
        binding.etTelefono.setText(user.telefono.orEmpty())
        binding.etPais.setText(user.pais.orEmpty())
        binding.etCiudad.setText(user.ciudad.orEmpty())
        binding.etBiografia.setText(user.biografia.orEmpty())
        if (!state.profession.isNullOrBlank()) {
            binding.actvProfesion.setText(state.profession, false)
        }

        currentAvatarUrl = user.avatarUrl
        val seed = user.nombres.ifBlank { sessionManager.getUsername() ?: "techmentor" }
        Glide.with(this)
            .load(AvatarUrls.of(seed, user.avatarUrl))
            .circleCrop()
            .placeholder(R.drawable.ic_user)
            .into(binding.ivAvatarPreview)

        // Guardar en sesión para futuras aperturas
        sessionManager.saveUserProfile(
            nombres = user.nombres,
            apellidos = user.apellidos,
            email = user.email,
            telefono = user.telefono,
            pais = user.pais,
            ciudad = user.ciudad,
            fechaNacimiento = user.fechaNacimiento,
            biografia = user.biografia,
            avatarUrl = user.avatarUrl
        )
    }

    private fun setupDropdowns() {
        val profesiones = listOf(
            "Desarrollador Web",
            "Ingeniero de Software",
            "Diseñador UX/UI",
            "Data Scientist",
            "DevOps",
            "Desarrollador Móvil"
        )
        binding.actvProfesion.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, profesiones)
        )
    }

    private fun setupListeners() {
        // --- Botón Atrás Seguro: Evita cierres forzosos usando NavController ---
        binding.cvBack.setOnClickListener {
            navigateBackSafely()
        }

        binding.cvAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tvCambiarFoto.setOnClickListener {
            val seed = (sessionManager.getUsername() ?: "techmentor") + "-" + Random.nextInt(100_000)
            currentAvatarUrl = AvatarUrls.random(seed)
            Glide.with(this)
                .load(currentAvatarUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivAvatarPreview)
        }

        binding.tilFechaNac.setEndIconOnClickListener { mostrarCalendario() }
        binding.etFechaNac.setOnClickListener { mostrarCalendario() }

        // --- Guardado de perfil ---
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
        val fechaNacDisplay = binding.etFechaNac.text?.toString()?.trim().orEmpty()

        if (nombres.isBlank()) {
            binding.tilNombres.error = "Los nombres son requeridos"
            return
        }
        binding.tilNombres.error = null

        val fechaIso = toIsoDate(fechaNacDisplay)

        val request = UpdateProfileRequestDto(
            nombres = nombres.ifBlank { null },
            apellidos = apellidos.ifBlank { null },
            fechaNacimiento = fechaIso,
            avatarUrl = currentAvatarUrl,
            biografia = biografia.ifBlank { null },
            telefono = telefono.ifBlank { null },
            pais = pais.ifBlank { null },
            ciudad = ciudad.ifBlank { null }
        )

        binding.btnSaveProfile.isEnabled = false

        vm.updateProfile(request) { ok, error ->
            if (!isAdded) return@updateProfile
            binding.btnSaveProfile.isEnabled = true
            if (ok) {
                sessionManager.saveUserProfile(
                    nombres = nombres,
                    apellidos = apellidos,
                    email = sessionManager.getProfileEmail(),
                    telefono = telefono,
                    pais = pais,
                    ciudad = ciudad,
                    fechaNacimiento = fechaIso,
                    biografia = biografia,
                    avatarUrl = currentAvatarUrl
                )
                Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                navigateBackSafely()
            } else {
                Toast.makeText(requireContext(), error ?: "No se pudo guardar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
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
