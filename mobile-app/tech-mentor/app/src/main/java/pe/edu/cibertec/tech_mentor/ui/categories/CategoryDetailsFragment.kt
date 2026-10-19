package pe.edu.cibertec.tech_mentor.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.databinding.FragmentCategoryDetailsBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCourseBinding

import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRepository: CatalogRepository
    private var categoryId: Long = 1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryId = arguments?.getLong("idCategory", 1L)
            ?: (arguments?.getInt("idCategory", 1)?.toLong() ?: 1L)

        setupRepository()
        loadCategoryDetails(categoryId)
    }

    private fun setupRepository() {
        // --- Versión anterior simulada (comentada): ---
        // catalogRepository = FakeCatalogRepository()

        // --- Versión real conectada a la API de TechMentor: ---
        val sessionManager = SessionManager(requireContext())
        val catalogApi = ApiClient.getClient(sessionManager).create(CatalogApiService::class.java)
        catalogRepository = CatalogRepositoryImpl(catalogApi)
    }

    private fun loadCategoryDetails(idCategory: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            val categoriesDeferred = async { catalogRepository.getCategories() }
            val coursesDeferred = async { catalogRepository.getCourses(idCategory) }

            val categoriesResult = categoriesDeferred.await()
            val coursesResult = coursesDeferred.await()

            categoriesResult.onSuccess { categories ->
                val selectedCategory = categories.find { it.idCategoria == idCategory }
                    ?: categories.firstOrNull()

                selectedCategory?.let { category ->
                    binding.tvRouteTitle.text = category.nombre
                    binding.tvRouteDescription.text = category.descripcion
                }
            }

            coursesResult.onSuccess { courses ->
                setupCoursesRecyclerView(courses)
            }.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar cursos: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupCoursesRecyclerView(courses: List<Course>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            Toast.makeText(
                requireContext(),
                "Curso seleccionado: ${selectedCourse.nombre}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvRouteCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRouteCourses.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CourseAdapter(
    private val courses: List<Course>,
    private val onCourseClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private var selectedIndex = RecyclerView.NO_POSITION

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course, isSelected: Boolean) {
            // --- Versión anterior con nombres en inglés (comentada): ---
            // binding.tvCourseTitle.text = course.name
            // binding.tvCourseSubtitle.text = course.description.ifEmpty { "+${course.rewardXp} XP al completar" }

            // --- Propiedades del modelo de dominio TechMentor (nombre, descripcion, xpRecompensa): ---
            binding.tvCourseTitle.text = course.nombre
            binding.tvCourseSubtitle.text = course.descripcion.ifEmpty {
                "+${course.xpRecompensa} XP al completar"
            }

            binding.root.isChecked = isSelected
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                val previous = selectedIndex
                selectedIndex = position
                if (previous != RecyclerView.NO_POSITION) notifyItemChanged(previous)
                notifyItemChanged(selectedIndex)
                onCourseClick(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position], position == selectedIndex)
    }

    override fun getItemCount(): Int = courses.size
}