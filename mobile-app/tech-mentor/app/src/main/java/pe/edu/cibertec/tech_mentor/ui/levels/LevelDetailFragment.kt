package pe.edu.cibertec.tech_mentor.ui.levels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.databinding.FragmentLevelDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemLessonBinding

data class Lesson(
    val title: String,
    val isViewed: Boolean
)

class LevelDetailFragment : Fragment() {

    private var _binding: FragmentLevelDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLevelInfo()
        setupLessonsRecyclerView()
    }

    private fun setupLevelInfo() {
        binding.tvLevelName.text = "Nivel Básico"
        binding.tvLevelDescription.text = "Aprende las bases fundamentales. Entenderemos desde la sintaxis básica hasta la creación de tus primeros objetos para que avances con seguridad."
    }

    private fun setupLessonsRecyclerView() {
        val fakeLessons = listOf(
            Lesson("1. Introducción al curso con un título muy largo para probar cómo se ve cuando supera las dos líneas permitidas por el diseño y pone los puntos suspensivos", true),
            Lesson("2. Manejo de variables y tipos de datos", true),
            Lesson("3. Estructuras de control (If/Else)", false),
            Lesson("4. Ciclos y bucles (For/While)", false),
            Lesson("5. Manejo de clases y funciones", false),
            Lesson("6. Introducción al curso con un título muy largo para probar cómo se ve cuando supera las dos líneas permitidas por el diseño y pone los puntos suspensivos", true),
            Lesson("7. Manejo de variables y tipos de datos", true),
            Lesson("8. Estructuras de control (If/Else)", false),
            Lesson("9. Ciclos y bucles (For/While)", false),
            Lesson("10. Manejo de clases y funciones", false),
            Lesson("11. Introducción al curso con un título muy largo para probar cómo se ve cuando supera las dos líneas permitidas por el diseño y pone los puntos suspensivos", true),
            Lesson("12. Manejo de variables y tipos de datos", true),
            Lesson("13. Estructuras de control (If/Else)", false),
            Lesson("14. Ciclos y bucles (For/While)", false),
            Lesson("15. Manejo de clases y funciones", false),
        )

        val adapter = LessonAdapter(fakeLessons)
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class LessonAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            binding.tvLessonTitle.text = lesson.title

            if (lesson.isViewed) {
                binding.root.alpha = 1.0f
            } else {
                binding.root.alpha = 0.4f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount(): Int = lessons.size
}