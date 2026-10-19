package pe.edu.cibertec.tech_mentor.ui.levels

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
import pe.edu.cibertec.tech_mentor.databinding.FragmentLevelDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemLessonBinding
import pe.edu.cibertec.tech_mentor.domain.model.Lesson
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

// Versión anterior (comentada): data class local con datos ficticios
// data class Lesson(
//     val title: String,
//     val isViewed: Boolean
// )

class LevelDetailFragment : Fragment() {

    private var _binding: FragmentLevelDetailsBinding? = null
    private val binding get() = _binding!!

    // Faltaba: repositorio y parámetros para cargar datos reales
    private lateinit var catalogRepository: CatalogRepository
    private var idNivel: Long = 0L
    private var idCurso: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Faltaba: leer los argumentos del bundle pasado desde CourseDetailFragment
        idNivel = arguments?.getLong("idNivel", 0L) ?: 0L
        idCurso = arguments?.getLong("idCurso", 0L) ?: 0L
        val levelNombre = arguments?.getString("levelNombre", "") ?: ""
        val levelDescripcion = arguments?.getString("levelDescripcion", "") ?: ""

        // Faltaba: mostrar datos del nivel recibidos por argumentos (rápido, sin esperar API)
        if (levelNombre.isNotBlank()) {
            binding.tvLevelName.text = levelNombre
        }
        if (levelDescripcion.isNotBlank()) {
            binding.tvLevelDescription.text = levelDescripcion
        }

        setupRepository()
        loadLevelLessons()
    }

    // Faltaba: configurar el repositorio real
    private fun setupRepository() {
        val sessionManager = SessionManager(requireContext())
        val catalogApi = ApiClient.getClient(sessionManager).create(CatalogApiService::class.java)
        catalogRepository = CatalogRepositoryImpl(catalogApi)
    }

    // Faltaba: cargar lecciones reales del nivel desde el backend usando /catalogo/lecciones?idNivel=
    private fun loadLevelLessons() {
        if (idNivel == 0L) {
            Toast.makeText(requireContext(), "Error: nivel no especificado", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Faltaba: también cargar el detalle del nivel para actualizar nombre y descripción
            val levelResult = catalogRepository.getLevelById(idNivel)
            levelResult.onSuccess { level ->
                binding.tvLevelName.text = level.nombre
                binding.tvLevelDescription.text = level.descripcion
            }

            // Faltaba: cargar lecciones del nivel
            val lessonsResult = catalogRepository.getLessons(idNivel)
            lessonsResult.onSuccess { lessons ->
                setupLessonsRecyclerView(lessons)
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar lecciones: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Faltaba: adapter con navegación real a LessonFragment
    private fun setupLessonsRecyclerView(lessons: List<Lesson>) {
        // Versión anterior (comentada): usaba lista ficticia sin click ni navegación
        // val fakeLessons = listOf(
        //     Lesson("1. Introducción al curso con un título muy largo...", true),
        //     Lesson("2. Manejo de variables y tipos de datos", true),
        //     ...
        // )
        // val adapter = LessonAdapter(fakeLessons)

        // Versión adaptada: usa datos reales y navega a LessonFragment
        val adapter = LessonApiAdapter(lessons) { selectedLesson ->
            // Faltaba: navegar a LessonFragment pasando los datos de la lección
            val bundle = bundleOf(
                "idLeccion" to selectedLesson.idLeccion,
                "idNivel" to idNivel,
                "titulo" to selectedLesson.titulo,
                "descripcion" to selectedLesson.descripcion,
                "contenido" to selectedLesson.contenido,
                "orden" to selectedLesson.orden,
                "xpRecompensa" to selectedLesson.xpRecompensa
            )
            findNavController().navigate(
                R.id.action_levelDetailFragment_to_lessonFragment,
                bundle
            )
        }
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Versión anterior (comentada): LessonAdapter con datos ficticios y sin click listener de navegación
// class LessonAdapter(private val lessons: List<Lesson>) :
//     RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
//     inner class LessonViewHolder(val binding: ItemLessonBinding) : ... {
//         fun bind(lesson: Lesson) {
//             binding.tvLessonTitle.text = lesson.title
//             if (lesson.isViewed) { binding.root.alpha = 1.0f } else { binding.root.alpha = 0.4f }
//         }
//     }
// }

// Versión adaptada: adapter con datos reales de Lesson y callback para navegar a LessonFragment
class LessonApiAdapter(
    private val lessons: List<Lesson>,
    private val onLessonClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonApiAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            // Faltaba: mostrar el título real de la lección con su número de orden
            binding.tvLessonTitle.text = "${lesson.orden}. ${lesson.titulo}"

            // Faltaba: click listener para navegar a la lección
            binding.root.setOnClickListener {
                onLessonClick(lesson)
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