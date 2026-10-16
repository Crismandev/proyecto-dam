package pe.edu.cibertec.tech_mentor.ui.practice

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentExamBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemExerciseOptionBinding

data class ExamOption(
    val letter: String,
    val text: String,
    val isCorrect: Boolean,
    var isSelected: Boolean = false
)

class ExamFragment : Fragment() {

    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!

    private lateinit var optionsAdapter: ExamOptionAdapter
    private var isAnswerChecked = false

    private val fakeOptions = listOf(
        ExamOption("A", "Una base de datos relacional", isCorrect = false),
        ExamOption("B", "Un conjunto de reglas para que dos sistemas se comuniquen", isCorrect = true),
        ExamOption("C", "Un entorno de ejecución en el navegador", isCorrect = false),
        ExamOption("D", "Un protocolo exclusivo de diseño visual", isCorrect = false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExamData()
        setupOptionsRecyclerView()
        setupListeners()
    }

    private fun setupExamData() {
        binding.tvExamContext.text = "DIAGNÓSTICO · FUNDAMENTOS DE APIS"
        binding.tvExamQuestion.text = "¿Qué es una API?"
        binding.tvExamQuestionHelper.text = "Selecciona la respuesta que mejor describa el concepto."

        binding.pbExamProgress.max = 3
        binding.pbExamProgress.progress = 1
        binding.tvExamProgressText.text = "1 de 3"

        binding.btnCheckExamAnswer.isEnabled = false
    }

    private fun setupOptionsRecyclerView() {
        optionsAdapter = ExamOptionAdapter(fakeOptions) { hasSelection ->
            if (hasSelection && !isAnswerChecked) {
                binding.btnCheckExamAnswer.isEnabled = true
            }
        }
        binding.rvExamOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExamOptions.adapter = optionsAdapter
    }

    private fun setupListeners() {
        binding.cvCloseExam.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCheckExamAnswer.setOnClickListener {
            val selectedOption = fakeOptions.find { it.isSelected } ?: return@setOnClickListener
            val context = requireContext()

            if (!isAnswerChecked) {
                isAnswerChecked = true
                binding.tvExamEncouragement.visibility = View.GONE
                binding.cvExamFeedback.visibility = View.VISIBLE
                binding.btnCheckExamAnswer.text = "Siguiente pregunta"

                val explanationDB = "Una API define cómo un sistema puede pedir información o acciones a otro de forma estructurada."

                if (selectedOption.isCorrect) {
                    binding.cvExamFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                    binding.tvExamFeedbackTitle.text = "¡Respuesta correcta!"
                    binding.tvExamFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
                    binding.tvExamFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.text_dark))
                } else {
                    binding.cvExamFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                    binding.tvExamFeedbackTitle.text = "Vamos a repasarlo."
                    binding.tvExamFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvExamFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.text_dark))
                }

                binding.tvExamFeedbackExplanation.text = explanationDB
            } else {
                // Aquí avanzarás a la siguiente pregunta o a ExamResultFragment
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ExamOptionAdapter(
    private val options: List<ExamOption>,
    private val onSelectionChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<ExamOptionAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(val binding: ItemExerciseOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: ExamOption) {
            val context = binding.root.context
            binding.tvOptionLetter.text = option.letter
            binding.tvOptionText.text = option.text

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

            binding.root.setOnClickListener {
                options.forEach { it.isSelected = false }
                option.isSelected = true
                notifyDataSetChanged()
                onSelectionChanged(true)
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