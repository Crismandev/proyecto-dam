package pe.edu.cibertec.tech_mentor.ui.practice

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentPracticeBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemPracticeEvaluationBinding

enum class PracticeFilter {
    ALL, DRAFTS, RESULTS
}

data class EvaluationItem(
    val id: Int,
    val courseName: String,
    val evaluationTitle: String,
    val isDraft: Boolean,
    val metricsText: String,
    val dateText: String
)

class PracticeFragment : Fragment() {

    private var _binding: FragmentPracticeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PracticeEvaluationAdapter
    private var currentFilter: PracticeFilter = PracticeFilter.ALL
    private var currentSearchQuery: String = ""

    private val allEvaluations = listOf(
        EvaluationItem(
            id = 1,
            courseName = "Desarrollo Web con React",
            evaluationTitle = "Evaluación: Componentes y Props",
            isDraft = true,
            metricsText = "Pendiente · 2 de 5 preguntas respondidas",
            dateText = "Hace 2 h"
        ),
        EvaluationItem(
            id = 4,
            courseName = "Desarrollo Móvil con Android",
            evaluationTitle = "Evaluación: Ciclo de vida de un Fragment",
            isDraft = true,
            metricsText = "Pendiente · 1 de 4 preguntas respondidas",
            dateText = "Hace 5 días"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        setupFilterButtons()
        setupDemoSyncCard()
        // Faltaba: listener del botón flotante "Nueva práctica" para iniciar el flujo de examen
        setupFabNewPractice()

        applyFilters()
    }

    // Faltaba: función que conecta el FAB "Nueva práctica" al flujo ExamList → Exam → ExamResult
    private fun setupFabNewPractice() {
        binding.fabNewPractice.setOnClickListener {
            // Versión anterior (comentada): el FAB no tenía listener — no hacía nada al presionarlo
            // (no existía setOnClickListener para fabNewPractice)

            // Faltaba: navegar a la lista de exámenes de diagnóstico al presionar "Nueva práctica"
            findNavController().navigate(R.id.action_practiceFragment_to_examListFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = PracticeEvaluationAdapter(emptyList()) { selectedItem ->
            // Versión anterior (comentada): solo mostraba un Toast
            // val action = if (selectedItem.isDraft) "Retomando borrador" else "Revisando resultado"
            // Toast.makeText(requireContext(), "$action: ${selectedItem.courseName}", Toast.LENGTH_SHORT).show()

            // Faltaba: navegar a la lista de exámenes al hacer clic en un elemento
            findNavController().navigate(R.id.action_practiceFragment_to_examListFragment)
        }
        binding.rvEvaluations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvaluations.adapter = adapter
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

    private fun setupFilterButtons() {
        binding.cvFilterAll.setOnClickListener {
            currentFilter = PracticeFilter.ALL
            updateFilterPillsUI()
            applyFilters()
        }

        binding.cvFilterDrafts.setOnClickListener {
            currentFilter = PracticeFilter.DRAFTS
            updateFilterPillsUI()
            applyFilters()
        }

        binding.cvFilterResults.setOnClickListener {
            currentFilter = PracticeFilter.RESULTS
            updateFilterPillsUI()
            applyFilters()
        }

        binding.btnExploreCourses.setOnClickListener {
            binding.etSearchCourse.setText("")
            currentFilter = PracticeFilter.ALL
            updateFilterPillsUI()
            applyFilters()
        }
    }

    private fun updateFilterPillsUI() {
        val activeBg = Color.parseColor("#5B4DFB")
        val inactiveBg = Color.WHITE
        val inactiveStroke = Color.parseColor("#EAEAF4")
        val activeText = Color.WHITE
        val inactiveText = Color.parseColor("#7A7A9D")

        listOf(
            binding.cvFilterAll to binding.tvFilterAll,
            binding.cvFilterDrafts to binding.tvFilterDrafts,
            binding.cvFilterResults to binding.tvFilterResults
        ).forEach { (card, text) ->
            card.setCardBackgroundColor(inactiveBg)
            card.strokeColor = inactiveStroke
            text.setTextColor(inactiveText)
        }

        when (currentFilter) {
            PracticeFilter.ALL -> {
                binding.cvFilterAll.setCardBackgroundColor(activeBg)
                binding.cvFilterAll.strokeColor = activeBg
                binding.tvFilterAll.setTextColor(activeText)
            }
            PracticeFilter.DRAFTS -> {
                binding.cvFilterDrafts.setCardBackgroundColor(activeBg)
                binding.cvFilterDrafts.strokeColor = activeBg
                binding.tvFilterDrafts.setTextColor(activeText)
            }
            PracticeFilter.RESULTS -> {
                binding.cvFilterResults.setCardBackgroundColor(activeBg)
                binding.cvFilterResults.strokeColor = activeBg
                binding.tvFilterResults.setTextColor(activeText)
            }
        }
    }

    private fun applyFilters() {
        val filteredList = allEvaluations.filter { item ->
            val matchesTab = when (currentFilter) {
                PracticeFilter.ALL -> true
                PracticeFilter.DRAFTS -> item.isDraft
                PracticeFilter.RESULTS -> !item.isDraft
            }

            val matchesSearch = if (currentSearchQuery.isEmpty()) {
                true
            } else {
                item.courseName.contains(currentSearchQuery, ignoreCase = true) ||
                        item.evaluationTitle.contains(currentSearchQuery, ignoreCase = true)
            }

            matchesTab && matchesSearch
        }

        adapter.updateData(filteredList)

        if (filteredList.isEmpty()) {
            binding.rvEvaluations.visibility = View.GONE
            binding.llEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvEvaluations.visibility = View.VISIBLE
            binding.llEmptyState.visibility = View.GONE
        }
    }

    private fun setupDemoSyncCard() {
        val draftsCount = allEvaluations.count { it.isDraft }
        binding.tvSyncSubtitle.text = "$draftsCount pendientes. Este botón simula la confirmación; no transmite información a un servidor."

        binding.cvSyncDemo.setOnClickListener {
            Toast.makeText(requireContext(), "Simulación: Resultados sincronizados correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PracticeEvaluationAdapter(
    private var items: List<EvaluationItem>,
    private val onItemClick: (EvaluationItem) -> Unit
) : RecyclerView.Adapter<PracticeEvaluationAdapter.EvaluationViewHolder>() {

    inner class EvaluationViewHolder(val binding: ItemPracticeEvaluationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EvaluationItem) {
            binding.tvCourseName.text = item.courseName
            binding.tvEvaluationTitle.text = item.evaluationTitle
            binding.tvEvaluationMetrics.text = item.metricsText
            binding.tvEvaluationDate.text = item.dateText

            if (item.isDraft) {
                binding.tvStatusBadge.text = "BORRADOR"
                binding.tvStatusBadge.setTextColor(Color.parseColor("#D25B22"))
                binding.cvStatusBadge.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
                binding.tvActionLink.text = "Retomar →"
            } else {
                binding.tvStatusBadge.text = "RESULTADO"
                binding.tvStatusBadge.setTextColor(Color.parseColor("#198754"))
                binding.cvStatusBadge.setCardBackgroundColor(Color.parseColor("#E8F5E9"))
                binding.tvActionLink.text = "Revisar →"
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    fun updateData(newItems: List<EvaluationItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationViewHolder {
        val binding = ItemPracticeEvaluationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EvaluationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvaluationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}