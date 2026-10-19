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
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
import pe.edu.cibertec.tech_mentor.util.AvatarUrls
import pe.edu.cibertec.tech_mentor.util.SessionManager
import java.util.Calendar
import kotlin.random.Random

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

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

        setupDropdowns()
        setupListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { bindProfile(it) }
            }
        }
        vm.refresh()
    }

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
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        binding.btnSaveProfile.setOnClickListener {
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
