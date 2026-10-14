package pe.edu.cibertec.tech_mentor.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentExerciseResultBinding

class ExerciseResultFragment : Fragment() {

    private var _binding: FragmentExerciseResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupResultData()
        setupListeners()
    }

    private fun setupResultData() {
        val context = binding.root.context

        val correctAnswers = 8
        val totalQuestions = 10
        val earnedXp = 30

        binding.tvCorrectAnswers.text = "$correctAnswers/$totalQuestions"
        binding.tvEarnedXp.text = earnedXp.toString()

        val isSuccess = correctAnswers > (totalQuestions / 2)

        if (isSuccess) {
            binding.cvResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.ivResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
            binding.ivResultIcon.setImageResource(R.drawable.ic_confetti)

            binding.tvResultSubtitle.text = "UN PASO MÁS EN TU RUTA"
            binding.tvResultTitle.text = "¡Lección completada!"
            binding.tvResultDescription.text = "Entendiste lo esencial. Ahora toca seguir construyendo."

            binding.cvNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.tvNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.tvNextStepTitle.text = "Tu siguiente paso"
            binding.tvNextStepDesc.text = "Continúa con un reto práctico de esta ruta."

        } else {
            binding.cvResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.ivResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
            binding.ivResultIcon.setImageResource(R.drawable.ic_arrow_counter_clockwise)

            binding.tvResultSubtitle.text = "NO TE DESANIMES"
            binding.tvResultTitle.text = "¡Sigue practicando!"
            binding.tvResultDescription.text = "Los errores son parte del aprendizaje. Repasa la teoría y vuelve a intentarlo."

            binding.cvNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.tvNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvNextStepTitle.text = "Recomendación"
            binding.tvNextStepDesc.text = "Revisa la lección anterior para afianzar estos conceptos."
        }
    }

    private fun setupListeners() {
        binding.btnReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}