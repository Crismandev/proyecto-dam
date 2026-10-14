package pe.edu.cibertec.tech_mentor.ui.exercises

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
import pe.edu.cibertec.tech_mentor.databinding.FragmentExercisesBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemExerciseOptionBinding

data class ExerciseOption(
    val letter: String,
    val text: String,
    val isCorrect: Boolean,
    var isSelected: Boolean = false
)

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

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

        setupExerciseData()
        setupOptionsRecyclerView()
        setupListeners()
    }

    private fun setupExerciseData() {
        binding.tvLessonContext.text = "FUNDAMENTOS DE APIS"
        binding.tvQuestion.text = "¿Qué es una API?"
        binding.tvQuestionHelper.text = "Piensa en cómo se conectan tus aplicaciones."

        binding.pbExerciseProgress.max = 3
        binding.pbExerciseProgress.progress = 1
        binding.tvProgressText.text = "1 de 3"

        binding.btnCheckAnswer.isEnabled = false
    }

    private fun setupOptionsRecyclerView() {
        val fakeOptions = listOf(
            ExerciseOption("A", "Una base de datos", isCorrect = false),
            ExerciseOption("B", "Un conjunto de reglas para que dos sistemas se comuniquen", isCorrect = true),
            ExerciseOption("C", "Un lenguaje de programación", isCorrect = false),
            ExerciseOption("D", "Un servidor web", isCorrect = false)
        )

        optionsAdapter = ExerciseOptionAdapter(fakeOptions) { hasSelection ->
            if (hasSelection) {
                binding.btnCheckAnswer.setBackgroundColor(Color.parseColor("#6F42C1"))
                binding.btnCheckAnswer.isEnabled = true
            }
        }

        binding.rvOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOptions.adapter = optionsAdapter
    }

    private fun setupListeners() {
        binding.cvClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCheckAnswer.setOnClickListener {
            val context = binding.root.context

            binding.tvEncouragement.visibility = View.GONE
            binding.cvFeedback.visibility = View.VISIBLE
            binding.btnCheckAnswer.text = "Siguiente pregunta →"

            val esRespuestaCorrecta = false
            val explicacionDB = "Una API define cómo un sistema puede pedir información o acciones a otro. Por ejemplo, una app del clima consulta datos a un servicio."

            if (esRespuestaCorrecta) {
                binding.cvFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
                binding.tvFeedbackTitle.text = context.getString(R.string.feedback_correct_title)
                binding.tvFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.tvFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                binding.cvFeedback.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_red))
                binding.tvFeedbackTitle.text = context.getString(R.string.feedback_incorrect_title)
                binding.tvFeedbackTitle.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.tvFeedbackExplanation.setTextColor(ContextCompat.getColor(context, R.color.red))
            }

            binding.tvFeedbackExplanation.text = explicacionDB
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ExerciseOptionAdapter(
    private val options: List<ExerciseOption>,
    private val onSelectionChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<ExerciseOptionAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(val binding: ItemExerciseOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: ExerciseOption) {
            binding.tvOptionLetter.text = option.letter
            binding.tvOptionText.text = option.text

            val context = binding.root.context

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