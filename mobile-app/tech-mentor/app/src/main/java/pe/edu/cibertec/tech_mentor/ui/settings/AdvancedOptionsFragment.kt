package pe.edu.cibertec.tech_mentor.ui.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pe.edu.cibertec.tech_mentor.databinding.DialogDeleteAccountBinding
import pe.edu.cibertec.tech_mentor.databinding.FragmentAdvancedOptionsBinding

class AdvancedOptionsFragment : Fragment() {

    private var _binding: FragmentAdvancedOptionsBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvancedOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.cvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cvDeleteAccount.setOnClickListener {
            mostrarDialogoEliminarCuenta()
        }
    }

    private fun mostrarDialogoEliminarCuenta() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogDeleteAccountBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        dialogBinding.btnConfirmDelete.isEnabled = false
        dialogBinding.btnConfirmDelete.alpha = 0.5f

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000) + 1
                dialogBinding.btnConfirmDelete.text = "Eliminar mi cuenta ($secondsLeft)"
            }

            override fun onFinish() {
                dialogBinding.btnConfirmDelete.text = "Eliminar mi cuenta"
                dialogBinding.btnConfirmDelete.isEnabled = true
                dialogBinding.btnConfirmDelete.alpha = 1.0f
            }
        }.start()

        dialogBinding.btnCancelDelete.setOnClickListener {
            countDownTimer?.cancel()
            dialog.dismiss()
        }

        dialogBinding.btnConfirmDelete.setOnClickListener {
            countDownTimer?.cancel()
            dialog.dismiss()
            Toast.makeText(
                requireContext(),
                "Solicitud enviada. Tienes 30 días para deshacerlo escribiéndonos.",
                Toast.LENGTH_LONG
            ).show()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}