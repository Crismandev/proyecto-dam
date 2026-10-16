package pe.edu.cibertec.tech_mentor.ui.practice

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentExamListBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemExamBinding

data class DiagnosticExamItem(
    val idExamen: Int,
    val idCurso: Int,
    val courseName: String,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)

class ExamListFragment : Fragment() {

    private var _binding: FragmentExamListBinding? = null
    private val binding get() = _binding!!

    private lateinit var examAdapter: ExamListAdapter
    private var selectedCourseId: Int? = null
    private var currentSearchQuery: String = ""

    private val rawExamsFromDb = listOf(
        DiagnosticExamItem(
            idExamen = 1,
            idCurso = 101,
            courseName = "Desarrollo Web con React",
            nombre = "Diagnóstico: Fundamentos de React y JSX",
            descripcion = "Descubre tu dominio sobre componentes, estado y props para ubicarte en el nivel adecuado.",
            activo = true
        ),
        DiagnosticExamItem(
            idExamen = 2,
            idCurso = 101,
            courseName = "Desarrollo Web con React",
            nombre = "Diagnóstico: Hooks y Ciclo de Vida",
            descripcion = "Evalúa tu experiencia manejando efectos secundarios y estado complejo en aplicaciones web.",
            activo = true
        ),
        DiagnosticExamItem(
            idExamen = 3,
            idCurso = 102,
            courseName = "Bases de datos con MySQL",
            nombre = "Diagnóstico: Modelado y Consultas SQL",
            descripcion = "Mide tus conocimientos en creación de tablas, llaves foráneas, JOINs y agrupación de datos.",
            activo = true
        ),
        DiagnosticExamItem(
            idExamen = 4,
            idCurso = 103,
            courseName = "Fundamentos de APIs",
            nombre = "Diagnóstico: Arquitectura REST y HTTP",
            descripcion = "Comprueba qué tanto sabes sobre verbos HTTP, códigos de estado y consumo de servicios JSON.",
            activo = true
        ),
        DiagnosticExamItem(
            idExamen = 5,
            idCurso = 104,
            courseName = "Desarrollo Móvil con Android",
            nombre = "Diagnóstico: Kotlin y Layouts XML",
            descripcion = "Ubica tu nivel inicial construyendo interfaces nativas y manejando actividades y fragmentos.",
            activo = true
        ),
        DiagnosticExamItem(
            idExamen = 6,
            idCurso = 102,
            courseName = "Bases de datos con MySQL",
            nombre = "Diagnóstico Antiguo (Deshabilitado)",
            descripcion = "Este examen tiene activo = false en la BD, por lo que no debe mostrarse.",
            activo = false
        )
    )

    private val activeExams: List<DiagnosticExamItem> by lazy {
        rawExamsFromDb.filter { it.activo }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCourseFilterPills()
        setupSearchInput()

        applyFilters()
    }

    private fun setupRecyclerView() {
        examAdapter = ExamListAdapter(emptyList()) { selectedExam ->
            Toast.makeText(
                requireContext(),
                "Iniciando: ${selectedExam.nombre}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvExams.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExams.adapter = examAdapter
    }

    private fun setupCourseFilterPills() {
        val distinctCourses = activeExams
            .map { it.idCurso to it.courseName }
            .distinctBy { it.first }

        binding.llCoursePillsContainer.removeAllViews()

        val allPill = createFilterPill(title = "Todos", courseId = null)
        binding.llCoursePillsContainer.addView(allPill)

        distinctCourses.forEach { (idCurso, courseName) ->
            val pill = createFilterPill(title = courseName, courseId = idCurso)
            binding.llCoursePillsContainer.addView(pill)
        }

        updateFilterPillsUI()
    }

    private fun createFilterPill(title: String, courseId: Int?): MaterialCardView {
        val context = requireContext()
        val card = MaterialCardView(context).apply {
            tag = courseId
            radius = dpToPx(12).toFloat()
            cardElevation = 0f
            strokeWidth = dpToPx(1)
            isClickable = true
            isFocusable = true

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dpToPx(38)
            ).apply {
                marginEnd = dpToPx(8)
            }
            layoutParams = params
        }

        val textView = TextView(context).apply {
            text = title
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            gravity = Gravity.CENTER
            setPadding(dpToPx(16), 0, dpToPx(16), 0)
            typeface = ResourcesCompat.getFont(context, R.font.plus_jakarta_sans_bold)
        }

        card.addView(textView)

        card.setOnClickListener {
            selectedCourseId = courseId
            updateFilterPillsUI()
            applyFilters()
        }

        return card
    }

    private fun updateFilterPillsUI() {
        val activeBg = Color.parseColor("#5B4DFB")
        val inactiveBg = Color.WHITE
        val inactiveStroke = Color.parseColor("#EAEAF4")
        val activeText = Color.WHITE
        val inactiveText = Color.parseColor("#7A7A9D")

        for (i in 0 until binding.llCoursePillsContainer.childCount) {
            val card = binding.llCoursePillsContainer.getChildAt(i) as MaterialCardView
            val textView = card.getChildAt(0) as TextView
            val isSelected = card.tag == selectedCourseId

            if (isSelected) {
                card.setCardBackgroundColor(activeBg)
                card.strokeColor = activeBg
                textView.setTextColor(activeText)
            } else {
                card.setCardBackgroundColor(inactiveBg)
                card.strokeColor = inactiveStroke
                textView.setTextColor(inactiveText)
            }
        }
    }

    private fun setupSearchInput() {
        binding.etSearchCourse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s?.toString()?.trim().orEmpty()
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun applyFilters() {
        val filtered = activeExams.filter { exam ->
            val matchesCoursePill = selectedCourseId == null || exam.idCurso == selectedCourseId

            val matchesSearch = if (currentSearchQuery.isEmpty()) {
                true
            } else {
                exam.courseName.contains(currentSearchQuery, ignoreCase = true) ||
                        exam.nombre.contains(currentSearchQuery, ignoreCase = true) ||
                        exam.descripcion.contains(currentSearchQuery, ignoreCase = true)
            }

            matchesCoursePill && matchesSearch
        }

        examAdapter.updateData(filtered)

        if (filtered.isEmpty()) {
            binding.rvExams.visibility = View.GONE
            binding.llEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvExams.visibility = View.VISIBLE
            binding.llEmptyState.visibility = View.GONE
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ExamListAdapter(
    private var exams: List<DiagnosticExamItem>,
    private val onExamClick: (DiagnosticExamItem) -> Unit
) : RecyclerView.Adapter<ExamListAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(val binding: ItemExamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exam: DiagnosticExamItem) {
            binding.tvCourseName.text = exam.courseName
            binding.tvExamName.text = exam.nombre
            binding.tvExamDescription.text = exam.descripcion

            binding.root.setOnClickListener {
                onExamClick(exam)
            }
        }
    }

    fun updateData(newExams: List<DiagnosticExamItem>) {
        exams = newExams
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val binding = ItemExamBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bind(exams[position])
    }

    override fun getItemCount(): Int = exams.size
}