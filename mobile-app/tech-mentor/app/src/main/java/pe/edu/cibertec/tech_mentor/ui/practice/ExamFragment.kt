package pe.edu.cibertec.tech_mentor.ui.practice

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
import pe.edu.cibertec.tech_mentor.data.remote.api.DiagnosticApiService
import pe.edu.cibertec.tech_mentor.data.remote.onFailure
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.repository.DiagnosticRepositoryImpl
import pe.edu.cibertec.tech_mentor.databinding.FragmentExamBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemExerciseOptionBinding
import pe.edu.cibertec.tech_mentor.domain.model.ExamAnswerSubmission
import pe.edu.cibertec.tech_mentor.domain.model.PublicDiagnosticOption
import pe.edu.cibertec.tech_mentor.domain.model.PublicQuestion
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

data class UiExamOption(
    val idOpcion: Long,
    val letter: String,
    val text: String,
    var isSelected: Boolean = false,
    var isCorrect: Boolean? = null
)

data class DiagnosticEvaluation(
    val isCorrect: Boolean,
    val correctOptionId: Long?,
    val explanation: String
)

class ExamFragment : Fragment() {

    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!

    private lateinit var diagnosticRepository: DiagnosticRepository

    private var idExamen: Long = 1L
    private var examTitle: String = ""
    private var courseName: String = ""

    private val questionsList = mutableListOf<PublicQuestion>()
    private var currentQuestionIndex = 0
    private val userAnswers = mutableMapOf<Long, Long>() // idPregunta -> idOpcionSeleccionada
    private var isAnswerChecked = false

    private lateinit var optionsAdapter: ExamOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idArg = arguments?.getLong("idExamenLong", 0L) ?: 0L
        idExamen = if (idArg != 0L) idArg else (arguments?.getInt("idExamen", 1)?.toLong() ?: 1L)
        examTitle = arguments?.getString("examNombre", "").orEmpty()
        courseName = arguments?.getString("courseName", "").orEmpty()

        setupRepository()
        setupListeners()
        loadExamQuestions()
    }

    private fun setupRepository() {
        val sessionManager = SessionManager(requireContext())
        val diagnosticApi = ApiClient.getClient(sessionManager).create(DiagnosticApiService::class.java)
        diagnosticRepository = DiagnosticRepositoryImpl(diagnosticApi)
    }

    private fun loadExamQuestions() {
        showLoadingState()

        viewLifecycleOwner.lifecycleScope.launch {
            val result = diagnosticRepository.getExamToTake(idExamen)
            result.onSuccess { examData ->
                if (examTitle.isBlank()) {
                    examTitle = examData.examen.nombre
                }
                questionsList.clear()
                questionsList.addAll(examData.preguntas.sortedBy { it.orden })

                if (questionsList.isNotEmpty()) {
                    currentQuestionIndex = 0
                    userAnswers.clear()
                    displayQuestion(currentQuestionIndex)
                } else {
                    useFallbackQuestions()
                }
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Cargando preguntas de diagnóstico (${error.message ?: "Modo local"})...",
                    Toast.LENGTH_SHORT
                ).show()
                useFallbackQuestions()
            }
        }
    }

    private fun useFallbackQuestions() {
        questionsList.clear()
        questionsList.addAll(getFallbackQuestions(idExamen))
        currentQuestionIndex = 0
        userAnswers.clear()
        displayQuestion(currentQuestionIndex)
    }

    private fun showLoadingState() {
        binding.pbExamProgress.isIndeterminate = true
        binding.tvExamProgressText.text = "Cargando..."
        binding.tvExamContext.text = if (examTitle.isNotBlank()) examTitle.uppercase() else "EVALUACIÓN DIAGNÓSTICA"
        binding.tvExamQuestion.text = "Preparando preguntas..."
        binding.tvExamQuestionHelper.text = "Por favor espera mientras cargamos tu evaluación."
        binding.btnCheckExamAnswer.isEnabled = false
        binding.cvExamFeedback.visibility = View.GONE
        binding.tvExamEncouragement.visibility = View.VISIBLE
    }

    private fun displayQuestion(index: Int) {
        if (index !in questionsList.indices) return

        val question = questionsList[index]
        val total = questionsList.size

        binding.pbExamProgress.isIndeterminate = false
        binding.pbExamProgress.max = total
        binding.pbExamProgress.progress = index + 1
        binding.tvExamProgressText.text = "${index + 1} de $total"

        val headerText = if (examTitle.isNotBlank()) "DIAGNÓSTICO · $examTitle" else "EVALUACIÓN DIAGNÓSTICA"
        binding.tvExamContext.text = headerText.uppercase()
        binding.tvExamQuestion.text = question.enunciado
        binding.tvExamQuestionHelper.text = "Selecciona la respuesta que mejor describa el concepto."

        isAnswerChecked = false
        binding.tvExamEncouragement.visibility = View.VISIBLE
        binding.cvExamFeedback.visibility = View.GONE

        binding.btnCheckExamAnswer.text = "Comprobar"
        binding.btnCheckExamAnswer.isEnabled = false

        val previouslySelectedId = userAnswers[question.idPregunta]
        val sortedOptions = question.opciones.sortedBy { it.orden }
        val uiOptions = sortedOptions.mapIndexed { optIdx, op ->
            val letter = ('A'.code + optIdx).toChar().toString()
            val friendlyText = formatFriendlyOptionText(question.enunciado, op.texto, op.orden)
            UiExamOption(
                idOpcion = op.idOpcionDiagnostico,
                letter = letter,
                text = friendlyText,
                isSelected = (op.idOpcionDiagnostico == previouslySelectedId),
                isCorrect = null
            )
        }

        optionsAdapter = ExamOptionAdapter(uiOptions) { selectedOption ->
            if (!isAnswerChecked) {
                userAnswers[question.idPregunta] = selectedOption.idOpcion
                binding.btnCheckExamAnswer.isEnabled = true
            }
        }

        binding.rvExamOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExamOptions.adapter = optionsAdapter

        if (uiOptions.any { it.isSelected }) {
            binding.btnCheckExamAnswer.isEnabled = true
        }
    }

    private fun formatFriendlyOptionText(enunciado: String, originalText: String, orden: Int): String {
        val lower = enunciado.lowercase()
        val isGeneric = originalText.startsWith("Correcta", ignoreCase = true) ||
                originalText.startsWith("Distractor", ignoreCase = true)

        if (!isGeneric) return originalText

        return when {
            lower.contains("enlace") -> {
                when (orden) {
                    1 -> "<a href=\"...\">Texto del enlace</a>"
                    2 -> "<link rel=\"stylesheet\" href=\"...\">"
                    else -> "<href url=\"...\">"
                }
            }
            lower.contains("estilo") -> {
                when (orden) {
                    1 -> "CSS (Cascading Style Sheets)"
                    2 -> "HTML (HyperText Markup Language)"
                    else -> "SQL (Structured Query Language)"
                }
            }
            lower.contains("dos caminos") -> {
                when (orden) {
                    1 -> "Estructura condicional if / else"
                    2 -> "Bucle iterativo for / while"
                    else -> "Declaración de interfaz o clase"
                }
            }
            lower.contains("constante") -> {
                when (orden) {
                    1 -> "final"
                    2 -> "const"
                    else -> "static"
                }
            }
            lower.contains("32 bits") -> {
                when (orden) {
                    1 -> "int"
                    2 -> "short"
                    else -> "long"
                }
            }
            lower.contains("mientras se cumpla") -> {
                when (orden) {
                    1 -> "while"
                    2 -> "switch"
                    else -> "try / catch"
                }
            }
            lower.contains("for (int i = 0") -> {
                when (orden) {
                    1 -> "3 veces (para i = 0, 1 y 2)"
                    2 -> "4 veces"
                    else -> "2 veces"
                }
            }
            lower.contains("obtiene filas") -> {
                when (orden) {
                    1 -> "SELECT"
                    2 -> "FETCH TABLE"
                    else -> "GET ROWS"
                }
            }
            lower.contains("filtra filas") -> {
                when (orden) {
                    1 -> "WHERE"
                    2 -> "GROUP BY"
                    else -> "ORDER BY"
                }
            }
            else -> originalText
        }
    }

    private fun evaluateDiagnosticAnswer(question: PublicQuestion, selectedOptionId: Long): DiagnosticEvaluation {
        val selectedOption = question.opciones.find { it.idOpcionDiagnostico == selectedOptionId }
        val correctOption = question.opciones.find {
            it.orden == 1 || it.texto.equals("Correcta", ignoreCase = true)
        } ?: question.opciones.firstOrNull()

        val isCorrect = selectedOption?.idOpcionDiagnostico == correctOption?.idOpcionDiagnostico
        val enunciado = question.enunciado.lowercase()

        val explanation = when {
            enunciado.contains("enlace") -> {
                if (isCorrect) "¡Correcto! En HTML, la etiqueta <a> (anchor) define un hipervínculo utilizando el atributo href."
                else "Incorrecto. La etiqueta <a> con su atributo href es la encargada de definir enlaces en HTML."
            }
            enunciado.contains("estilo") -> {
                if (isCorrect) "¡Correcto! CSS (Cascading Style Sheets) es el lenguaje estándar utilizado para el diseño y estilo visual de las páginas."
                else "Incorrecto. CSS es el lenguaje que aplica estilos, colores y tipografía a la estructura HTML."
            }
            enunciado.contains("dos caminos") -> {
                if (isCorrect) "¡Correcto! Las estructuras condicionales (if / else) permiten bifurcar el flujo de ejecución evaluando una condición lógica."
                else "Incorrecto. Las estructuras condicionales como if-else permiten decidir entre dos o más caminos de ejecución."
            }
            enunciado.contains("constante") -> {
                if (isCorrect) "¡Correcto! En Java, la palabra clave 'final' se usa para definir variables cuyo valor no puede ser modificado."
                else "Incorrecto. La palabra clave 'final' en Java se utiliza para declarar constantes inmutables."
            }
            enunciado.contains("32 bits") -> {
                if (isCorrect) "¡Correcto! El tipo primitivo 'int' en Java guarda números enteros de 32 bits."
                else "Incorrecto. En Java, 'int' representa enteros de 32 bits, mientras 'short' es de 16 y 'long' de 64 bits."
            }
            enunciado.contains("mientras se cumpla") -> {
                if (isCorrect) "¡Correcto! El ciclo 'while' ejecuta su bloque de código repetidamente mientras su condición sea verdadera."
                else "Incorrecto. La estructura 'while' repite código mientras se cumpla la condición especificada."
            }
            enunciado.contains("for (int i = 0") -> {
                if (isCorrect) "¡Correcto! El bucle corre para los valores i = 0, i = 1 e i = 2, completando un total de 3 iteraciones."
                else "Incorrecto. Comenzando en 0 y terminando antes de 3, el bucle se ejecuta exactamente 3 veces."
            }
            enunciado.contains("obtiene filas") -> {
                if (isCorrect) "¡Correcto! La sentencia SELECT se utiliza para consultar y obtener registros de tablas en bases de datos SQL."
                else "Incorrecto. SELECT es el comando estándar de SQL para recuperar filas y datos."
            }
            enunciado.contains("filtra filas") -> {
                if (isCorrect) "¡Correcto! La cláusula WHERE filtra las filas devueltas basándose en condiciones de comparación."
                else "Incorrecto. WHERE es la cláusula que aplica condiciones y filtros en consultas SQL."
            }
            else -> {
                if (isCorrect) "¡Respuesta correcta! Has identificado la alternativa indicada para esta pregunta."
                else "La opción seleccionada no es la correcta. Revisa el tema para afianzar este concepto."
            }
        }

        return DiagnosticEvaluation(isCorrect, correctOption?.idOpcionDiagnostico, explanation)
    }

    private fun setupListeners() {
        binding.cvCloseExam.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCheckExamAnswer.setOnClickListener {
            if (questionsList.isEmpty()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return@setOnClickListener
            }

            val currentQuestion = questionsList[currentQuestionIndex]
            val total = questionsList.size
            val context = binding.root.context

            if (!isAnswerChecked) {
                // Primer clic: evaluar respuesta y mostrar feedback
                val selected = optionsAdapter.getSelectedOption() ?: return@setOnClickListener
                userAnswers[currentQuestion.idPregunta] = selected.idOpcion
                isAnswerChecked = true

                val evaluation = evaluateDiagnosticAnswer(currentQuestion, selected.idOpcion)

                // Resaltar opciones: verde la correcta, rojo la seleccionada errónea
                optionsAdapter.showFeedback(
                    selectedOptionId = selected.idOpcion,
                    correctOptionId = evaluation.correctOptionId
                )

                binding.tvExamEncouragement.visibility = View.GONE
                binding.cvExamFeedback.visibility = View.VISIBLE

                if (evaluation.isCorrect) {
                    binding.cvExamFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                    binding.tvExamFeedbackTitle.text = "¡Respuesta correcta!"
                    binding.tvExamFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
                    binding.tvExamFeedbackExplanation.text = evaluation.explanation
                    binding.tvExamFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.green))
                } else {
                    binding.cvExamFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                    binding.tvExamFeedbackTitle.text = "Vamos a repasarlo."
                    binding.tvExamFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvExamFeedbackExplanation.text = evaluation.explanation
                    binding.tvExamFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                if (currentQuestionIndex == total - 1) {
                    binding.btnCheckExamAnswer.text = "Ver resultado →"
                } else {
                    binding.btnCheckExamAnswer.text = "Siguiente pregunta →"
                }
                binding.btnCheckExamAnswer.isEnabled = true
            } else {
                // Segundo clic: avanzar a la siguiente pregunta o enviar resultados
                if (currentQuestionIndex < total - 1) {
                    currentQuestionIndex++
                    displayQuestion(currentQuestionIndex)
                } else {
                    submitExamProgress()
                }
            }
        }
    }

    private fun submitExamProgress() {
        binding.btnCheckExamAnswer.isEnabled = false
        binding.btnCheckExamAnswer.text = "Guardando resultado..."

        viewLifecycleOwner.lifecycleScope.launch {
            val submissions = questionsList.map { q ->
                ExamAnswerSubmission(
                    idPregunta = q.idPregunta,
                    idOpcionSeleccionada = userAnswers[q.idPregunta]
                )
            }

            val actualCorrectCount = questionsList.count { q ->
                val selectedId = userAnswers[q.idPregunta]
                selectedId != null && evaluateDiagnosticAnswer(q, selectedId).isCorrect
            }
            val totalQuestions = questionsList.size

            val result = diagnosticRepository.submitExam(idExamen, submissions)
            result.onSuccess { diagnosticResult ->
                val correct = diagnosticResult.puntajeObtenido
                val max = if (diagnosticResult.puntajeMaximo > 0) diagnosticResult.puntajeMaximo else totalQuestions
                val nivelAsignado = diagnosticResult.nombreNivelAsignado

                val bundle = Bundle().apply {
                    putInt("idExamen", idExamen.toInt())
                    putString("examNombre", examTitle)
                    putInt("correctAnswers", correct)
                    putInt("totalQuestions", max)
                    putInt("earnedXp", if (correct > max / 2) 50 else 20)
                    putString("recommendedLevel", nivelAsignado.orEmpty())
                }
                findNavController().navigate(
                    R.id.action_examFragment_to_examResultFragment,
                    bundle
                )
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Examen finalizado (${error.message ?: "Conectividad"}).",
                    Toast.LENGTH_SHORT
                ).show()

                val bundle = Bundle().apply {
                    putInt("idExamen", idExamen.toInt())
                    putString("examNombre", examTitle)
                    putInt("correctAnswers", actualCorrectCount)
                    putInt("totalQuestions", totalQuestions)
                    putInt("earnedXp", if (actualCorrectCount > totalQuestions / 2) 50 else 20)
                    putString("recommendedLevel", if (actualCorrectCount > totalQuestions / 2) "Intermedio" else "Básico")
                }
                findNavController().navigate(
                    R.id.action_examFragment_to_examResultFragment,
                    bundle
                )
            }
        }
    }

    private fun getFallbackQuestions(examenId: Long): List<PublicQuestion> {
        return if (examenId == 2L) {
            // Diagnóstico Programación (6 preguntas de datos.sql)
            listOf(
                PublicQuestion(
                    idPregunta = 1L, idCategoriaPregunta = 1L,
                    enunciado = "¿Qué palabra clave declara una constante en Java?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 1,
                    opciones = listOf(
                        PublicDiagnosticOption(1L, "final", 1),
                        PublicDiagnosticOption(2L, "const", 2),
                        PublicDiagnosticOption(3L, "static", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 2L, idCategoriaPregunta = 1L,
                    enunciado = "¿Qué tipo de dato guarda enteros de 32 bits en Java?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 2,
                    opciones = listOf(
                        PublicDiagnosticOption(4L, "int", 1),
                        PublicDiagnosticOption(5L, "short", 2),
                        PublicDiagnosticOption(6L, "long", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 3L, idCategoriaPregunta = 2L,
                    enunciado = "¿Qué estructura repite código mientras se cumpla una condición?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 3,
                    opciones = listOf(
                        PublicDiagnosticOption(7L, "while", 1),
                        PublicDiagnosticOption(8L, "if", 2),
                        PublicDiagnosticOption(9L, "switch", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 4L, idCategoriaPregunta = 2L,
                    enunciado = "¿Cuántas veces corre: for (int i = 0; i < 3; i++)?",
                    nivelDificultad = "INTERMEDIO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 4,
                    opciones = listOf(
                        PublicDiagnosticOption(10L, "3 veces", 1),
                        PublicDiagnosticOption(11L, "4 veces", 2),
                        PublicDiagnosticOption(12L, "2 veces", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 5L, idCategoriaPregunta = 3L,
                    enunciado = "¿Qué sentencia SQL obtiene filas de una tabla?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 5,
                    opciones = listOf(
                        PublicDiagnosticOption(13L, "SELECT", 1),
                        PublicDiagnosticOption(14L, "FETCH", 2),
                        PublicDiagnosticOption(15L, "GET", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 6L, idCategoriaPregunta = 3L,
                    enunciado = "¿Qué cláusula filtra filas en una consulta SQL?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 6,
                    opciones = listOf(
                        PublicDiagnosticOption(16L, "WHERE", 1),
                        PublicDiagnosticOption(17L, "GROUP BY", 2),
                        PublicDiagnosticOption(18L, "ORDER BY", 3)
                    )
                )
            )
        } else {
            // Diagnóstico Desarrollo Web (3 preguntas de datos.sql)
            listOf(
                PublicQuestion(
                    idPregunta = 9L, idCategoriaPregunta = 4L,
                    enunciado = "¿Qué etiqueta HTML define un enlace?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 1,
                    opciones = listOf(
                        PublicDiagnosticOption(25L, "<a href=\"...\">", 1),
                        PublicDiagnosticOption(26L, "<link src=\"...\">", 2),
                        PublicDiagnosticOption(27L, "<href to=\"...\">", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 8L, idCategoriaPregunta = 4L,
                    enunciado = "¿Qué lenguaje da estilo a una página web?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 2,
                    opciones = listOf(
                        PublicDiagnosticOption(22L, "CSS (Cascading Style Sheets)", 1),
                        PublicDiagnosticOption(23L, "HTML (HyperText Markup)", 2),
                        PublicDiagnosticOption(24L, "SQL (Structured Query)", 3)
                    )
                ),
                PublicQuestion(
                    idPregunta = 7L, idCategoriaPregunta = 2L,
                    enunciado = "¿Qué estructura permite decidir entre dos caminos?",
                    nivelDificultad = "BASICO", tipoPregunta = "OPCION_MULTIPLE", puntaje = 1, orden = 3,
                    opciones = listOf(
                        PublicDiagnosticOption(19L, "Estructura condicional if / else", 1),
                        PublicDiagnosticOption(20L, "Bucle iterativo for", 2),
                        PublicDiagnosticOption(21L, "Declaración de función", 3)
                    )
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ExamOptionAdapter(
    private val options: List<UiExamOption>,
    private val onSelectionChanged: (UiExamOption) -> Unit
) : RecyclerView.Adapter<ExamOptionAdapter.OptionViewHolder>() {

    fun getSelectedOption(): UiExamOption? = options.find { it.isSelected }

    fun showFeedback(selectedOptionId: Long, correctOptionId: Long?) {
        options.forEach { opt ->
            if (opt.idOpcion == selectedOptionId) {
                opt.isCorrect = (selectedOptionId == correctOptionId)
            } else if (opt.idOpcion == correctOptionId) {
                opt.isCorrect = true // Resaltar en verde la opción correcta
            } else {
                opt.isCorrect = null
            }
        }
        notifyDataSetChanged()
    }

    inner class OptionViewHolder(val binding: ItemExerciseOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: UiExamOption) {
            val context = binding.root.context
            binding.tvOptionLetter.text = option.letter
            binding.tvOptionText.text = option.text

            when (option.isCorrect) {
                true -> {
                    // Correcta: verde
                    binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.green)
                    binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                    binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                false -> {
                    // Incorrecta: rojo
                    binding.cvOptionContainer.strokeColor = ContextCompat.getColor(context, R.color.red)
                    binding.cvOptionContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                    binding.cvLetterBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvOptionLetter.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                null -> {
                    // Regular
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