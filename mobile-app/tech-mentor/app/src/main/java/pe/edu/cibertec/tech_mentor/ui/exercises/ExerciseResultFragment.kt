package pe.edu.cibertec.tech_mentor.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.databinding.FragmentExerciseResultBinding
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

class ExerciseResultFragment : Fragment() {

    private var _binding: FragmentExerciseResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressRepository: ProgressRepository
    private var idLeccion: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idLeccion = arguments?.getLong("idLeccion", 0L) ?: 0L
        val correctAnswers = arguments?.getInt("correctAnswers", -1) ?: -1
        val totalQuestions = arguments?.getInt("totalQuestions", -1) ?: -1
        val earnedXp = arguments?.getInt("earnedXp", -1) ?: -1
        val lessonTitle = arguments?.getString("lessonTitle", "") ?: ""

        setupRepository()

        if (correctAnswers >= 0 && totalQuestions >= 0 && earnedXp >= 0) {
            // Mostrar datos reales recibidos desde ExercisesFragment tras completar la lección en la API
            displayResult(correctAnswers, totalQuestions, earnedXp, lessonTitle)
        } else if (idLeccion != 0L) {
            // Fallback: consultar el avance real de la lección desde la API
            loadResultFromApi()
        }

        setupListeners()
    }

    private fun setupRepository() {
        val sessionManager = SessionManager(requireContext())
        val progressApi = ApiClient.getClient(sessionManager).create(ProgressApiService::class.java)
        progressRepository = ProgressRepositoryImpl(progressApi)
    }

    private fun loadResultFromApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = progressRepository.getMyLessonsProgress()
            result.onSuccess { progressList ->
                val progress = progressList.find { it.idLeccion == idLeccion }
                if (progress != null) {
                    val total = 2
                    val displayCorrect = if (progress.puntaje > total) {
                        total
                    } else {
                        progress.puntaje
                    }
                    displayResult(
                        correctAnswers = displayCorrect,
                        totalQuestions = total,
                        earnedXp = progress.xpGanado,
                        lessonTitle = progress.tituloLeccion
                    )
                }
            }
        }
    }

    private fun displayResult(
        correctAnswers: Int,
        totalQuestions: Int,
        earnedXp: Int,
        lessonTitle: String
    ) {
        val context = binding.root.context

        // Evitar que aparezca un puntaje mayor al total (por ejemplo 20/2 por escala 20 en la BD)
        val sanitizedCorrect = if (totalQuestions > 0 && correctAnswers > totalQuestions) {
            if (correctAnswers == 20) totalQuestions
            else Math.min(totalQuestions, Math.round(correctAnswers.toDouble() / 20.0 * totalQuestions).toInt())
        } else {
            Math.max(0, correctAnswers)
        }

        binding.tvCorrectAnswers.text = "$sanitizedCorrect/$totalQuestions"
        binding.tvEarnedXp.text = earnedXp.toString()

        val isSuccess = if (totalQuestions > 0) sanitizedCorrect >= (totalQuestions / 2.0) else true

        if (isSuccess) {
            binding.cvResultIconBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
            binding.ivResultIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
            binding.ivResultIcon.setImageResource(R.drawable.ic_confetti)

            binding.tvResultSubtitle.text = "UN PASO MÁS EN TU RUTA"
            binding.tvResultTitle.text = "¡Lección completada!"
            binding.tvResultDescription.text = if (lessonTitle.isNotBlank()) {
                "Completaste \"$lessonTitle\" exitosamente ($sanitizedCorrect de $totalQuestions aciertos)."
            } else {
                "Entendiste lo esencial ($sanitizedCorrect de $totalQuestions aciertos). Ahora toca seguir construyendo."
            }

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
            binding.tvResultDescription.text = if (totalQuestions > 0) {
                "Obtuviste $sanitizedCorrect de $totalQuestions aciertos. Repasa la teoría y vuelve a intentarlo."
            } else {
                "Los errores son parte del aprendizaje. Repasa la teoría y vuelve a intentarlo."
            }

            binding.cvNextStepBg.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
            binding.tvNextStepTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvNextStepTitle.text = "Recomendación"
            binding.tvNextStepDesc.text = "Revisa la lección anterior para afianzar estos conceptos."
        }
    }

    private fun setupListeners() {
        binding.btnReturn.setOnClickListener {
            // Retroceder directamente a la lista de lecciones (levelDetailFragment)
            findNavController().popBackStack(R.id.levelDetailFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}