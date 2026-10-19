package pe.edu.cibertec.tech_mentor.ui.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentExamResultBinding

class ExamResultFragment : Fragment() {

    private var _binding: FragmentExamResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExamResultData()
        setupListeners()
    }

    private fun setupExamResultData() {
        val context = binding.root.context

        val correctAnswers = 3
        val totalQuestions = 3
        val earnedXp = 50

        binding.tvExamCorrectAnswers.text = "$correctAnswers/$totalQuestions"
        binding.tvExamEarnedXp.text = earnedXp.toString()

        val isSuccess = correctAnswers > (totalQuestions / 2)

        if (isSuccess) {
            binding.cvExamResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.ivExamResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))

            binding.tvExamResultSubtitle.text = "DIAGNÓSTICO FINALIZADO"
            binding.tvExamResultTitle.text = "¡Examen superado!"
            binding.tvExamResultDescription.text = "Demostraste un gran dominio de los conceptos clave de este curso."

            binding.cvExamNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.tvExamNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.tvExamNextStepTitle.text = "Nivel recomendado: Intermedio"
            binding.tvExamNextStepDesc.text = "Ya dominas las bases. Puedes avanzar directamente al siguiente módulo de tu ruta."
        } else {
            binding.cvExamResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.ivExamResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))

            binding.tvExamResultSubtitle.text = "DIAGNÓSTICO FINALIZADO"
            binding.tvExamResultTitle.text = "¡Buen punto de partida!"
            binding.tvExamResultDescription.text = "Identificamos los temas clave que te ayudarán a construir una base sólida."

            binding.cvExamNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.tvExamNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvExamNextStepTitle.text = "Nivel recomendado: Básico"
            binding.tvExamNextStepDesc.text = "Te recomendamos iniciar desde la primera lección del curso para afianzar estos conceptos."
        }
    }

    private fun setupListeners() {
        binding.btnExamReturn.setOnClickListener {
            // Versión anterior (comentada): usaba onBackPressed que solo retrocede un paso
            // requireActivity().onBackPressedDispatcher.onBackPressed()

            // Faltaba: retroceder directamente a Práctica (practiceFragment)
            findNavController().popBackStack(R.id.practiceFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}