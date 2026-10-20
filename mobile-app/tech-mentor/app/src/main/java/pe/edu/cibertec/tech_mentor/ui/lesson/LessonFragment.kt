package pe.edu.cibertec.tech_mentor.ui.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentLessonBinding
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    // Faltaba: repositorio y parámetros para cargar datos reales
    private lateinit var catalogRepository: CatalogRepository
    private var idLeccion: Long = 0L
    private var idCurso: Long = 0L
    private var idNivel: Long = 0L
    private var lessonTitulo: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Faltaba: leer los argumentos del bundle pasado desde LevelDetailFragment
        idLeccion = arguments?.getLong("idLeccion", 0L) ?: 0L
        idCurso = arguments?.getLong("idCurso", 0L) ?: 0L
        idNivel = arguments?.getLong("idNivel", 0L) ?: 0L
        val titulo = arguments?.getString("titulo", "") ?: ""
        lessonTitulo = titulo
        val descripcion = arguments?.getString("descripcion", "") ?: ""
        val contenido = arguments?.getString("contenido", "") ?: ""
        val orden = arguments?.getInt("orden", 0) ?: 0
        val xpRecompensa = arguments?.getInt("xpRecompensa", 0) ?: 0

        setupRepository()

        // Faltaba: mostrar datos recibidos por argumentos inmediatamente (sin esperar API)
        if (titulo.isNotBlank()) {
            setupLessonDataFromArgs(titulo, descripcion, contenido, orden)
        } else if (idLeccion != 0L) {
            // Si no se pasaron los datos, cargarlos desde la API
            loadLessonFromApi()
        }

        setupListeners()
    }

    // Faltaba: configurar el repositorio real
    private fun setupRepository() {
        val sessionManager = SessionManager(requireContext())
        val catalogApi = ApiClient.getClient(sessionManager).create(CatalogApiService::class.java)
        catalogRepository = CatalogRepositoryImpl(catalogApi)
    }

    // Faltaba: llenar los datos de la lección recibidos por argumentos
    private fun setupLessonDataFromArgs(
        titulo: String,
        descripcion: String,
        contenido: String,
        orden: Int
    ) {
        // Versión anterior (comentada): datos hardcodeados
        // binding.tvLessonOrder.text = "LECCIÓN 1"
        // binding.tvLessonTitle.text = "Introducción a las Variables"
        // binding.tvLessonDescription.text = "En esta lección aprenderemos..."
        // binding.tvLessonContent.text = "Imagina que una variable..."

        // Versión adaptada: datos reales de la lección
        binding.tvLessonOrder.text = "LECCIÓN $orden"
        binding.tvLessonTitle.text = titulo
        binding.tvLessonDescription.text = descripcion.ifEmpty {
            "Completa esta lección para avanzar en el curso."
        }
        binding.tvLessonContent.text = contenido.ifEmpty {
            "El contenido de esta lección estará disponible pronto."
        }
    }

    // Faltaba: cargar datos de lección desde la API si no vienen en el bundle
    private fun loadLessonFromApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = catalogRepository.getLessonById(idLeccion)
            result.onSuccess { lesson ->
                lessonTitulo = lesson.titulo
                idNivel = lesson.idNivel
                setupLessonDataFromArgs(
                    titulo = lesson.titulo,
                    descripcion = lesson.descripcion,
                    contenido = lesson.contenido,
                    orden = lesson.orden
                )
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar la lección: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupListeners() {
        binding.btnSolveExercises.setOnClickListener {
            // Versión anterior (comentada): solo mostraba un Toast
            // Toast.makeText(requireContext(), "¡Pronto podrás resolver los ejercicios de esta lección!", Toast.LENGTH_SHORT).show()

            // Faltaba: navegar a los ejercicios de la lección
            val bundle = android.os.Bundle().apply {
                putLong("idLeccion", idLeccion)
                putLong("idCurso", idCurso)
                putLong("idNivel", idNivel)
                putString("titulo", lessonTitulo)
            }
            findNavController().navigate(R.id.action_lessonFragment_to_exercisesFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}