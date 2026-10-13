package pe.edu.cibertec.tech_mentor.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.databinding.FragmentCategoryDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCourseBinding

// 1. Data Class temporal para los cursos de la ruta
data class Course(
    val title: String,
    val levelAndDuration: String
)

class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de la Cabecera de la Ruta (Datos Temporales)
        setupRouteDetails()

        // Configuración de la Lista de Cursos (Datos Temporales)
        setupCoursesRecyclerView()
    }

    private fun setupRouteDetails() {
        // Aquí simulamos que recibimos los datos de la ruta seleccionada
        binding.tvRouteTitle.text = "Desarrollo Web"
        binding.tvRouteDescription.text = "Desarrolla aplicaciones web desde cero y haz realidad tu carrera como Web Developer. Sé parte de la industria con los empleos mejor pagados a nivel global."
    }

    private fun setupCoursesRecyclerView() {
        // Datos ficticios de los cursos de la ruta
        val fakeCourses = listOf(
            Course("Desarrollo web con React", "Intermedio · Lecciones de 5 min"),
            Course("Bases de datos con MySQL", "Intermedio · Lecciones de 5 min"),
            Course("Programación orientada a objetos", "Principiante · Lecciones de 5 min"),
            Course("Desarrollo móvil con Android", "Principiante · Lecciones de 5 min"),
            Course("Fundamentos de HTML y CSS", "Principiante · Lecciones de 4 min"),
            Course("Introducción a JavaScript", "Principiante · Lecciones de 5 min")
        )

        val adapter = CourseAdapter(fakeCourses)
        binding.rvRouteCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRouteCourses.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 2. Adapter temporal creado en el mismo archivo para agilizar pruebas
class CourseAdapter(private val courses: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.title
            binding.tvCourseSubtitle.text = course.levelAndDuration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}