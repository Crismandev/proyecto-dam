package pe.edu.cibertec.tech_mentor.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pe.edu.cibertec.tech_mentor.databinding.FragmentEditCredentialsBinding

class EditCredentialsFragment : Fragment() {

    private var _binding: FragmentEditCredentialsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMockData()
        setupListeners()
    }

    private fun setupMockData() {
        binding.etNewEmail.setText("correo.nuevo@cibertec.edu.pe")
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnUpdateEmail.setOnClickListener {
            val newEmail = binding.etNewEmail.text.toString().trim()
            if (newEmail.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Correo actualizado correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnChangePassword.setOnClickListener {
            val currentPass = binding.etCurrentPassword.text.toString()
            val newPass = binding.etNewPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            when {
                currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty() -> {
                    Toast.makeText(requireContext(), "Completa todos los campos de contraseña", Toast.LENGTH_SHORT).show()
                }
                newPass != confirmPass -> {
                    Toast.makeText(requireContext(), "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
                    binding.etCurrentPassword.text?.clear()
                    binding.etNewPassword.text?.clear()
                    binding.etConfirmPassword.text?.clear()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}