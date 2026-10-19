package pe.edu.cibertec.tech_mentor.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentCourseDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCourseLevelBinding
import pe.edu.cibertec.tech_mentor.domain.model.LevelContent
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

// Versión anterior (comentada): data class local con datos ficticios
// data class CourseLevel(
//     val name: String,
//     val description: String
// )

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!

    // Faltaba: repositorio y id del curso para cargar datos reales
    private lateinit var catalogRepository: CatalogRepository
    private var idCurso: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Faltaba: leer el idCurso del bundle pasado desde CategoryDetailsFragment
        idCurso = arguments?.getLong("idCurso", 0L) ?: 0L

        setupRepository()
        loadCourseContent()
    }

    // Faltaba: configurar el repositorio real
    private fun setupRepository() {
        val sessionManager = SessionManager(requireContext())
        val catalogApi = ApiClient.getClient(sessionManager).create(CatalogApiService::class.java)
        catalogRepository = CatalogRepositoryImpl(catalogApi)
    }

    // Faltaba: cargar contenido real del curso desde el backend usando /catalogo/cursos/{idCurso}/contenido
    private fun loadCourseContent() {
        if (idCurso == 0L) {
            Toast.makeText(requireContext(), "Error: curso no especificado", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val result = catalogRepository.getCourseContent(idCurso)

            result.onSuccess { courseContent ->
                // Faltaba: llenar el banner con datos reales del curso
                binding.tvCourseTitle.text = courseContent.nombre
                binding.tvCourseDescription.text = courseContent.descripcion

                // Faltaba: cargar los niveles reales del curso
                setupLevelsRecyclerView(courseContent.niveles)
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar el curso: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()

                // Faltaba: fallback — intentar obtener el curso básico si el contenido falla
                loadCourseFallback()
            }
        }
    }

    // Faltaba: fallback usando getCourseById si getCourseContent falla
    private fun loadCourseFallback() {
        viewLifecycleOwner.lifecycleScope.launch {
            catalogRepository.getCourseById(idCurso).onSuccess { course ->
                binding.tvCourseTitle.text = course.nombre
                binding.tvCourseDescription.text = course.descripcion

                // Cargar niveles por separado si el endpoint de contenido falló
                loadLevelsSeparately()
            }
        }
    }

    // Faltaba: cargar niveles del curso por separado usando /catalogo/niveles?idCurso=
    private fun loadLevelsSeparately() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Usar getLevels filtrando por idCurso
            val levelsResult = catalogRepository.getLevels(idCurso)
            levelsResult.onSuccess { levels ->
                // Convertir Level a LevelContent para reutilizar el adapter
                val levelContents = levels.map { level ->
                    LevelContent(
                        idNivel = level.idNivel,
                        nombre = level.nombre,
                        descripcion = level.descripcion,
                        orden = level.orden,
                        lecciones = emptyList()
                    )
                }
                setupLevelsRecyclerView(levelContents)
            }.onFailure {
                Toast.makeText(requireContext(), "No se pudieron cargar los niveles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Faltaba: adapter con navegación real a LevelDetailFragment
    private fun setupLevelsRecyclerView(levels: List<LevelContent>) {
        // Versión anterior (comentada): usaba datos ficticios y CourseLevelAdapter sin navegación
        // val fakeLevels = listOf(
        //     CourseLevel("Nivel Básico", "Comienza aquí tu aprendizaje"),
        //     CourseLevel("Nivel Intermedio", "Profundiza en la semántica y estilos avanzados"),
        //     CourseLevel("Nivel Avanzado", "Domina animaciones y buenas prácticas profesionales")
        // )
        // val adapter = CourseLevelAdapter(fakeLevels)

        // Versión adaptada: usa datos reales y navega a LevelDetailFragment
        val adapter = CourseLevelApiAdapter(levels) { selectedLevel ->
            // Faltaba: navegar a LevelDetailFragment pasando idNivel y idCurso
            val bundle = bundleOf(
                "idNivel" to selectedLevel.idNivel,
                "idCurso" to idCurso,
                "levelNombre" to selectedLevel.nombre,
                "levelDescripcion" to selectedLevel.descripcion
            )
            findNavController().navigate(
                R.id.action_courseDetailFragment_to_levelDetailFragment,
                bundle
            )
        }
        binding.rvCourseLevels.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourseLevels.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Versión anterior (comentada): CourseLevelAdapter con datos ficticios sin navegación
// class CourseLevelAdapter(private val levels: List<CourseLevel>) :
//     RecyclerView.Adapter<CourseLevelAdapter.LevelViewHolder>() { ... }

// Versión adaptada: adapter con datos reales de LevelContent y callback de navegación
class CourseLevelApiAdapter(
    private val levels: List<LevelContent>,
    private val onLevelClick: (LevelContent) -> Unit
) : RecyclerView.Adapter<CourseLevelApiAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(val binding: ItemCourseLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(level: LevelContent, position: Int) {
            // Faltaba: mostrar el número de orden en el círculo
            binding.tvLevelOrder.text = (position + 1).toString()
            binding.tvLevelName.text = level.nombre
            binding.tvLevelDescription.text = level.descripcion.ifEmpty {
                "${level.lecciones.size} lecciones"
            }

            // Faltaba: click listener para navegar al nivel
            binding.root.setOnClickListener {
                onLevelClick(level)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemCourseLevelBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position], position)
    }

    override fun getItemCount(): Int = levels.size
}