package pe.edu.cibertec.tech_mentor.ui.courses // Ajusta el paquete si lo tienes en otra ruta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.databinding.FragmentCourseDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCourseLevelBinding

// 1. Data Class temporal para simular los niveles del curso
data class CourseLevel(
    val name: String,
    val description: String
)

class CourseDetailFragment : Fragment() {

    // Configuración segura de ViewBinding
    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. Llenar los datos del banner principal
        setupCourseBannerData()

        // 3. Configurar la lista de niveles
        setupLevelsRecyclerView()
    }

    private fun setupCourseBannerData() {
        // Datos simulados para el curso (esto reemplaza a los tools:text del XML)
        binding.tvCourseTitle.text = "Desarrollo Web con HTML y CSS"
        binding.tvCourseDescription.text = "Aprende a estructurar y dar estilo a páginas web desde cero. Domina las bases del frontend y crea tu primer sitio responsive."
    }

    private fun setupLevelsRecyclerView() {
        // Generamos los 3 niveles requeridos con datos ficticios
        val fakeLevels = listOf(
            CourseLevel("Nivel Básico", "Comienza aquí tu aprendizaje"),
            CourseLevel("Nivel Intermedio", "Profundiza en la semántica y estilos avanzados"),
            CourseLevel("Nivel Avanzado", "Domina animaciones y buenas prácticas profesionales")
        )

        // Conectamos el Adapter al RecyclerView
        val adapter = CourseLevelAdapter(fakeLevels)
        // Usamos requireContext() para el LayoutManager como en los otros fragmentos
        binding.rvCourseLevels.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourseLevels.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiamos el binding para evitar fugas de memoria
        _binding = null
    }
}

// 4. Adapter temporal creado en el mismo archivo para no tocar nada más
class CourseLevelAdapter(private val levels: List<CourseLevel>) :
    RecyclerView.Adapter<CourseLevelAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(val binding: ItemCourseLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(level: CourseLevel) {
            // Asignamos el nombre y la descripción del nivel a las vistas de la tarjeta
            binding.tvLevelName.text = level.name
            binding.tvLevelDescription.text = level.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemCourseLevelBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position])
    }

    override fun getItemCount(): Int = levels.size
}