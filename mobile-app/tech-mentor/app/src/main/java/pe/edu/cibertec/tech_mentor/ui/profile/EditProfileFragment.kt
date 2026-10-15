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
import com.bumptech.glide.Glide
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditProfileBinding
import java.util.Calendar

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

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

        setupMockData()
        setupDropdowns()
        setupListeners()
    }

    private fun setupMockData() {
        Glide.with(this)
            .load("https://avatars.githubusercontent.com/u/145881432?v=4")
            .circleCrop()
            .into(binding.ivAvatarPreview)

        binding.etNombres.setText("Yabet Mathias")
        binding.etApellidos.setText("Porras Vilca")
        binding.etFechaNac.setText("27/05/2000")
        binding.etTelefono.setText("987 654 321")
        binding.etPais.setText("Perú")
        binding.etCiudad.setText("Lima")
        binding.actvProfesion.setText("Desarrollador Web", false)
        binding.etBiografia.setText("Apasionado por el desarrollo web y móvil. Aprendiendo paso a paso junto a Bit para convertirme en Full Stack Developer.")
    }

    private fun setupDropdowns() {
        val profesiones = listOf("Desarrollador Web", "Ingeniero de Software", "Diseñador UX/UI", "Data Scientist", "DevOps")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, profesiones)
        binding.actvProfesion.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        binding.btnSaveProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
            binding.etFechaNac.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}