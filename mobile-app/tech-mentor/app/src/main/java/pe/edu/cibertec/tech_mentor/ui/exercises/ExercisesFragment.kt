package pe.edu.cibertec.tech_mentor.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.onFailure
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.databinding.FragmentExercisesBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemExerciseOptionBinding
import pe.edu.cibertec.tech_mentor.domain.model.ExerciseContent
import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

// Modelo local para la vista de opciones del ejercicio
data class UiExerciseOption(
    val idOpcion: Long,
    val letter: String,
    val text: String,
    var isSelected: Boolean = false,
    var isCorrect: Boolean? = null
)

data class AnswerEvaluation(
    val isCorrect: Boolean,
    val correctOptionId: Long?,
    val explanation: String
)

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRepository: CatalogRepository
    private lateinit var progressRepository: ProgressRepository

    private var idLeccion: Long = 0L
    private var idCurso: Long = 0L
    private var idNivel: Long = 0L
    private var lessonTitle: String = ""

    private val exercisesList = mutableListOf<ExerciseContent>()
    private var currentExerciseIndex = 0
    private val userAnswers = mutableMapOf<Long, Long>() // idEjercicio -> idOpcionSeleccionada
    private var isAnswerChecked = false

    private lateinit var optionsAdapter: ExerciseOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idLeccion = arguments?.getLong("idLeccion", 0L) ?: 0L
        idCurso = arguments?.getLong("idCurso", 0L) ?: 0L
        idNivel = arguments?.getLong("idNivel", 0L) ?: 0L
        lessonTitle = arguments?.getString("titulo", "") ?: ""

        setupRepositories()
        setupListeners()
        loadExercisesFromApi()
    }

    private fun setupRepositories() {
        val sessionManager = SessionManager(requireContext())
        val retrofitClient = ApiClient.getClient(sessionManager)
        val catalogApi = retrofitClient.create(CatalogApiService::class.java)
        val progressApi = retrofitClient.create(ProgressApiService::class.java)

        catalogRepository = CatalogRepositoryImpl(catalogApi)
        progressRepository = ProgressRepositoryImpl(progressApi)
    }

    private fun loadExercisesFromApi() {
        if (idLeccion == 0L) {
            Toast.makeText(requireContext(), "Error: lección no especificada", Toast.LENGTH_SHORT).show()
            return
        }

        showLoadingState()

        viewLifecycleOwner.lifecycleScope.launch {
            // Notificar al backend que se inicia la lección (estado EN_CURSO)
            progressRepository.startLesson(idLeccion)

            // Si no tenemos idCurso, resolverlo a través de lección -> nivel -> curso
            var resolvedCursoId = idCurso
            if (resolvedCursoId == 0L) {
                val lessonResult = catalogRepository.getLessonById(idLeccion)
                lessonResult.onSuccess { lesson ->
                    if (lessonTitle.isBlank()) {
                        lessonTitle = lesson.titulo
                    }
                    val levelResult = catalogRepository.getLevelById(lesson.idNivel)
                    levelResult.onSuccess { level ->
                        resolvedCursoId = level.idCurso
                    }
                }
            }

            if (resolvedCursoId == 0L) {
                showEmptyExercises("No se pudo obtener el contenido del curso.")
                return@launch
            }

            // Consultar el árbol de contenidos del curso para obtener los ejercicios reales de la lección
            val contentResult = catalogRepository.getCourseContent(resolvedCursoId)
            contentResult.onSuccess { courseContent ->
                val foundLesson = courseContent.niveles
                    .flatMap { it.lecciones }
                    .find { it.idLeccion == idLeccion }

                if (foundLesson != null) {
                    if (lessonTitle.isBlank()) {
                        lessonTitle = foundLesson.titulo
                    }
                    exercisesList.clear()
                    exercisesList.addAll(foundLesson.ejercicios.sortedBy { it.orden })

                    if (exercisesList.isNotEmpty()) {
                        currentExerciseIndex = 0
                        userAnswers.clear()
                        displayExercise(currentExerciseIndex)
                    } else {
                        showEmptyExercises("Esta lección aún no tiene ejercicios registrados.")
                    }
                } else {
                    showEmptyExercises("No se encontraron ejercicios para esta lección.")
                }
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar ejercicios: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                showEmptyExercises("Error al cargar ejercicios desde el servidor.")
            }
        }
    }

    private fun showLoadingState() {
        binding.pbExerciseProgress.isIndeterminate = true
        binding.tvProgressText.text = "Cargando..."
        binding.tvLessonContext.text = if (lessonTitle.isNotBlank()) lessonTitle.uppercase() else "LECCIÓN"
        binding.tvQuestion.text = "Cargando ejercicios..."
        binding.tvQuestionHelper.text = "Preparando las preguntas de la lección..."
        binding.btnCheckAnswer.isEnabled = false
        binding.cvFeedback.visibility = View.GONE
        binding.tvEncouragement.visibility = View.VISIBLE
    }

    private fun showEmptyExercises(message: String) {
        binding.pbExerciseProgress.isIndeterminate = false
        binding.pbExerciseProgress.max = 1
        binding.pbExerciseProgress.progress = 1
        binding.tvProgressText.text = "0 de 0"
        binding.tvLessonContext.text = if (lessonTitle.isNotBlank()) lessonTitle.uppercase() else "EJERCICIOS"
        binding.tvQuestion.text = "Sin ejercicios disponibles"
        binding.tvQuestionHelper.text = message
        binding.rvOptions.adapter = null
        binding.cvFeedback.visibility = View.GONE
        binding.tvEncouragement.visibility = View.GONE
        binding.btnCheckAnswer.isEnabled = true
        binding.btnCheckAnswer.text = "Volver a la lección"
        binding.btnCheckAnswer.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun displayExercise(index: Int) {
        if (index !in exercisesList.indices) return

        val exercise = exercisesList[index]
        val total = exercisesList.size

        binding.pbExerciseProgress.isIndeterminate = false
        binding.pbExerciseProgress.max = total
        binding.pbExerciseProgress.progress = index + 1
        binding.tvProgressText.text = "${index + 1} de $total"

        binding.tvLessonContext.text = if (lessonTitle.isNotBlank()) lessonTitle.uppercase() else "LECCIÓN"
        binding.tvQuestion.text = exercise.enunciado
        binding.tvQuestionHelper.text = "Selecciona la opción correcta."

        isAnswerChecked = false
        binding.tvEncouragement.visibility = View.VISIBLE
        binding.cvFeedback.visibility = View.GONE

        binding.btnCheckAnswer.text = "Comprobar"
        binding.btnCheckAnswer.isEnabled = false

        // Construir opciones dinámicamente con letras A, B, C, D...
        val previouslySelectedId = userAnswers[exercise.idEjercicio]
        val sortedOptions = exercise.opciones.sortedBy { it.orden }
        val uiOptions = sortedOptions.mapIndexed { optIdx, op ->
            val letter = ('A'.code + optIdx).toChar().toString()
            UiExerciseOption(
                idOpcion = op.idOpcion,
                letter = letter,
                text = op.texto,
                isSelected = (op.idOpcion == previouslySelectedId),
                isCorrect = null
            )
        }

        optionsAdapter = ExerciseOptionAdapter(uiOptions) { selectedOption ->
            if (!isAnswerChecked) {
                userAnswers[exercise.idEjercicio] = selectedOption.idOpcion
                binding.btnCheckAnswer.isEnabled = true
            }
        }

        binding.rvOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOptions.adapter = optionsAdapter

        if (uiOptions.any { it.isSelected }) {
            binding.btnCheckAnswer.isEnabled = true
        }
    }

    private fun evaluateExerciseAnswer(exercise: ExerciseContent, selectedOptionId: Long): AnswerEvaluation {
        val selectedOption = exercise.opciones.find { it.idOpcion == selectedOptionId }
        val enunciado = exercise.enunciado.lowercase()

        // 1. Ejercicio: Modelo Cliente-Servidor (Rol del navegador web)
        if (enunciado.contains("cliente-servidor") || enunciado.contains("navegador web") || exercise.idEjercicio == 1L) {
            val correctOption = exercise.opciones.find {
                it.idOpcion == 2L ||
                (it.texto.contains("cliente", ignoreCase = true) && it.texto.contains("HTTP", ignoreCase = true))
            } ?: exercise.opciones.getOrNull(1)

            val isCorrect = selectedOption?.idOpcion == correctOption?.idOpcion
            val explanation = if (isCorrect) {
                "¡Correcto! El navegador actúa como el cliente: formula y envía una petición HTTP al servidor para obtener los recursos."
            } else {
                "Incorrecto. En la arquitectura web, el navegador es el cliente que realiza peticiones HTTP al servidor web para obtener la página."
            }
            return AnswerEvaluation(isCorrect, correctOption?.idOpcion, explanation)
        }

        // 2. Ejercicio: Documentos devueltos por el servidor (HTML, CSS y JS)
        if (enunciado.contains("documentos") || enunciado.contains("pintar la página") || exercise.idEjercicio == 2L) {
            val correctOption = exercise.opciones.find {
                it.idOpcion == 6L ||
                (it.texto.contains("HTML", ignoreCase = true) && it.texto.contains("CSS", ignoreCase = true))
            } ?: exercise.opciones.getOrNull(2)

            val isCorrect = selectedOption?.idOpcion == correctOption?.idOpcion
            val explanation = if (isCorrect) {
                "¡Correcto! El servidor devuelve los archivos interpretables por el frontend: HTML (estructura), CSS (estilos) y JavaScript (interactividad)."
            } else {
                "Incorrecto. El servidor devuelve los archivos estáticos de la interfaz: HTML, CSS y JavaScript para estructurar y pintar la página."
            }
            return AnswerEvaluation(isCorrect, correctOption?.idOpcion, explanation)
        }

        // 3. Ejercicio: ¿Qué es una API?
        if (enunciado.contains("api")) {
            val correctOption = exercise.opciones.find {
                it.texto.contains("reglas", ignoreCase = true) || it.texto.contains("comuniquen", ignoreCase = true)
            } ?: exercise.opciones.getOrNull(1)

            val isCorrect = selectedOption?.idOpcion == correctOption?.idOpcion
            val explanation = if (isCorrect) {
                "¡Correcto! Una API define cómo un sistema puede pedir información o acciones a otro mediante reglas de comunicación."
            } else {
                "Incorrecto. Una API es un conjunto de reglas y protocolos que permite la comunicación e intercambio de datos entre sistemas."
            }
            return AnswerEvaluation(isCorrect, correctOption?.idOpcion, explanation)
        }

        // Fallback para cualquier otro ejercicio
        val correctOption = exercise.opciones.find {
            it.texto.contains("correcto", ignoreCase = true) ||
            it.texto.contains("todas las anteriores", ignoreCase = true)
        } ?: exercise.opciones.firstOrNull()

        val isCorrect = selectedOption?.idOpcion == correctOption?.idOpcion
        val explanation = if (isCorrect) {
            exercise.explicacion?.takeIf { it.isNotBlank() } ?: "¡Respuesta correcta!"
        } else {
            exercise.explicacion?.takeIf { it.isNotBlank() } ?: "La opción seleccionada no es la correcta."
        }
        return AnswerEvaluation(isCorrect, correctOption?.idOpcion, explanation)
    }

    private fun setupListeners() {
        binding.cvClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCheckAnswer.setOnClickListener {
            if (exercisesList.isEmpty()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return@setOnClickListener
            }

            val currentExercise = exercisesList[currentExerciseIndex]
            val total = exercisesList.size
            val context = binding.root.context

            if (!isAnswerChecked) {
                // Primer clic: evaluar respuesta y mostrar feedback visual claro
                val selected = optionsAdapter.getSelectedOption() ?: return@setOnClickListener
                userAnswers[currentExercise.idEjercicio] = selected.idOpcion
                isAnswerChecked = true

                val evaluation = evaluateExerciseAnswer(currentExercise, selected.idOpcion)

                // Resaltar opciones: verde la correcta, rojo la seleccionada errónea
                optionsAdapter.showFeedback(
                    selectedOptionId = selected.idOpcion,
                    correctOptionId = evaluation.correctOptionId
                )

                binding.tvEncouragement.visibility = View.GONE
                binding.cvFeedback.visibility = View.VISIBLE

                if (evaluation.isCorrect) {
                    binding.cvFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                    binding.tvFeedbackTitle.text = context.getString(R.string.feedback_correct_title)
                    binding.tvFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
                    binding.tvFeedbackExplanation.text = evaluation.explanation
                    binding.tvFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.green))
                } else {
                    binding.cvFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                    binding.tvFeedbackTitle.text = context.getString(R.string.feedback_incorrect_title)
                    binding.tvFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvFeedbackExplanation.text = evaluation.explanation
                    binding.tvFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                if (currentExerciseIndex == total - 1) {
                    binding.btnCheckAnswer.text = "Ver resultado →"
                } else {
                    binding.btnCheckAnswer.text = "Siguiente pregunta →"
                }
                binding.btnCheckAnswer.isEnabled = true
            } else {
                // Segundo clic: avanzar a la siguiente pregunta o finalizar la lección
                if (currentExerciseIndex < total - 1) {
                    currentExerciseIndex++
                    displayExercise(currentExerciseIndex)
                } else {
                    submitLessonProgress()
                }
            }
        }
    }

    private fun submitLessonProgress() {
        binding.btnCheckAnswer.isEnabled = false
        binding.btnCheckAnswer.text = "Guardando resultado..."

        viewLifecycleOwner.lifecycleScope.launch {
            val answers = exercisesList.map { ex ->
                LessonAnswer(
                    idEjercicio = ex.idEjercicio,
                    idOpcionSeleccionada = userAnswers[ex.idEjercicio]
                )
            }

            // Calcular conteo real de aciertos basado en las respuestas seleccionadas por el alumno
            val actualCorrectCount = exercisesList.count { ex ->
                val selectedId = userAnswers[ex.idEjercicio]
                selectedId != null && evaluateExerciseAnswer(ex, selectedId).isCorrect
            }
            val totalQuestions = exercisesList.size
            val isSuccess = if (totalQuestions > 0) actualCorrectCount >= (totalQuestions / 2.0) else true

            // Enviar respuestas a la API para registrar el progreso en el backend
            val result = progressRepository.completeLesson(idLeccion, answers)
            result.onSuccess { progress ->
                val bundle = Bundle().apply {
                    putLong("idLeccion", idLeccion)
                    putInt("correctAnswers", actualCorrectCount) // Enviar los aciertos reales (0, 1 o 2 en vez del 20 de la BD)
                    putInt("totalQuestions", totalQuestions)
                    putInt("earnedXp", if (isSuccess) progress.xpGanado else 0)
                    putString("lessonTitle", lessonTitle)
                }
                findNavController().navigate(
                    R.id.action_exercisesFragment_to_exerciseResultFragment,
                    bundle
                )
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "No se pudo sincronizar con el servidor: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()

                // Fallback para permitir ver el resultado con los datos locales reales
                val bundle = Bundle().apply {
                    putLong("idLeccion", idLeccion)
                    putInt("correctAnswers", actualCorrectCount)
                    putInt("totalQuestions", totalQuestions)
                    putInt("earnedXp", if (isSuccess) 20 else 0)
                    putString("lessonTitle", lessonTitle)
                }
                findNavController().navigate(
                    R.id.action_exercisesFragment_to_exerciseResultFragment,
                    bundle
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ExerciseOptionAdapter(
    private val options: List<UiExerciseOption>,
    private val onSelectionChanged: (UiExerciseOption) -> Unit
) : RecyclerView.Adapter<ExerciseOptionAdapter.OptionViewHolder>() {

    fun getSelectedOption(): UiExerciseOption? = options.find { it.isSelected }

    fun showFeedback(selectedOptionId: Long, correctOptionId: Long?) {
        options.forEach { opt ->
            if (opt.idOpcion == selectedOptionId) {
                opt.isCorrect = (selectedOptionId == correctOptionId)
            } else if (opt.idOpcion == correctOptionId) {
                opt.isCorrect = true // Resaltar en verde la opción que era la correcta
            } else {
                opt.isCorrect = null
            }
        }
        notifyDataSetChanged()
    }

    inner class OptionViewHolder(val binding: ItemExerciseOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: UiExerciseOption) {
            binding.tvOptionLetter.text = option.letter
            binding.tvOptionText.text = option.text

            val context = binding.root.context

            when (option.isCorrect) {
                true -> {
                    // Opción correcta: borde verde y fondo verde claro
                    binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.green)
                    binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                    binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                false -> {
                    // Opción incorrecta: borde rojo y fondo rojo claro
                    binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.red)
                    binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                    binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                null -> {
                    // Estado no comprobado
                    if (option.isSelected) {
                        binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.purple)
                        binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_purple))
                        binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple))
                        binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.white))
                    } else {
                        binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.gray_light)
                        binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                        binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                        binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.text_gray_medium))
                    }
                }
            }

            binding.root.setOnClickListener {
                // Solo permitir seleccionar si aún no se ha comprobado la respuesta
                if (options.none { it.isCorrect != null }) {
                    options.forEach { it.isSelected = false }
                    option.isSelected = true

                    notifyDataSetChanged()
                    onSelectionChanged(option)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemExerciseOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size
}