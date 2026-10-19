package pe.edu.cibertec.tech_mentor.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pe.edu.cibertec.tech_mentor.BuildConfig
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentSettingsBinding
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity
import pe.edu.cibertec.tech_mentor.util.SessionManager

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

        binding.tvAppVersion.text = getString(R.string.label_app_version, BuildConfig.VERSION_NAME)
        setupListeners()
    }

    private fun setupListeners() {
        binding.cvEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        binding.cvEditCredentials.setOnClickListener {
            Toast.makeText(requireContext(), "Editar credenciales", Toast.LENGTH_SHORT).show()
        }

        binding.cvAdvancedOptions.setOnClickListener {
            Toast.makeText(requireContext(), "Opciones avanzadas", Toast.LENGTH_SHORT).show()
        }

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

        binding.btnLogout.setOnClickListener {
            SessionManager(requireContext()).clearSession()
            Toast.makeText(requireContext(), R.string.msg_logout, Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
