package pe.edu.cibertec.tech_mentor.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pe.edu.cibertec.tech_mentor.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.cvEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Editar perfil", Toast.LENGTH_SHORT).show()
        }

        binding.cvEditCredentials.setOnClickListener {
            Toast.makeText(requireContext(), "Editar credenciales", Toast.LENGTH_SHORT).show()
        }

        binding.cvAdvancedOptions.setOnClickListener {
            Toast.makeText(requireContext(), "Opciones avanzadas", Toast.LENGTH_SHORT).show()
        }

        // Listeners Footer
        binding.btnFeedback.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Feedback", Toast.LENGTH_SHORT).show()
        }

        binding.btnContact.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Contáctanos", Toast.LENGTH_SHORT).show()
        }

        binding.btnTerms.setOnClickListener {
            Toast.makeText(requireContext(), "Términos y Condiciones", Toast.LENGTH_SHORT).show()
        }

        binding.btnPrivacy.setOnClickListener {
            Toast.makeText(requireContext(), "Políticas de Privacidad", Toast.LENGTH_SHORT).show()
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}