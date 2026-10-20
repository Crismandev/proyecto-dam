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

        val correctAnswers = arguments?.getInt("correctAnswers", 0) ?: 0
        val totalQuestions = (arguments?.getInt("totalQuestions", 0) ?: 0).let {
            if (it <= 0) 3 else it
        }
        val earnedXp = arguments?.getInt("earnedXp", if (correctAnswers > totalQuestions / 2) 50 else 20) ?: 20
        val recommendedLevelArg = arguments?.getString("recommendedLevel", "").orEmpty()

        binding.tvExamCorrectAnswers.text = "$correctAnswers/$totalQuestions"
        binding.tvExamEarnedXp.text = earnedXp.toString()

        val isSuccess = correctAnswers > (totalQuestions / 2)
        val recommendedLevel = if (recommendedLevelArg.isNotEmpty()) {
            recommendedLevelArg
        } else if (isSuccess) {
            "Intermedio"
        } else {
            "Básico"
        }

        if (isSuccess) {
            binding.cvExamResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.ivExamResultIcon.setImageResource(R.drawable.ic_trophy)
            binding.ivExamResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))

            binding.tvExamResultSubtitle.text = "DIAGNÓSTICO FINALIZADO"
            binding.tvExamResultTitle.text = if (correctAnswers == totalQuestions) "¡Examen perfecto!" else "¡Examen superado!"
            binding.tvExamResultDescription.text = "Demostraste un gran dominio respondiendo $correctAnswers de $totalQuestions preguntas correctamente."

            binding.cvExamNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.tvExamNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.tvExamNextStepTitle.text = "Nivel recomendado: $recommendedLevel"
            binding.tvExamNextStepDesc.text = "Ya dominas las bases. Puedes avanzar directamente al siguiente módulo de tu ruta de aprendizaje."
        } else {
            binding.cvExamResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.ivExamResultIcon.setImageResource(R.drawable.ic_sparkle)
            binding.ivExamResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))

            binding.tvExamResultSubtitle.text = "DIAGNÓSTICO FINALIZADO"
            binding.tvExamResultTitle.text = "¡Buen punto de partida!"
            binding.tvExamResultDescription.text = "Obtuviste $correctAnswers de $totalQuestions respuestas correctas. Identificamos los temas clave que te ayudarán a construir una base sólida."

            binding.cvExamNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.tvExamNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvExamNextStepTitle.text = "Nivel recomendado: $recommendedLevel"
            binding.tvExamNextStepDesc.text = "Te recomendamos iniciar desde las primeras lecciones del curso para afianzar estos conceptos."
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