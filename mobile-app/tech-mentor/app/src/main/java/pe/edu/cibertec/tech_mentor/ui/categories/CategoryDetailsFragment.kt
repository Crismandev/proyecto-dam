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

class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRepository: CatalogRepository
    private var categoryId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryId = arguments?.getInt("idCategory", 1) ?: 1

        setupRepository()
        loadCategoryDetails(categoryId)
    }

    private fun setupRepository() {
        // MODO PRUEBA
        catalogRepository = FakeCatalogRepository()

        // Descomentar cuando el backend esté listo:
        // val context = requireContext()
        // catalogRepository = CatalogRepositoryImpl(RetrofitClient.getCatalogApi(context))
    }

    private fun loadCategoryDetails(idCategory: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val categoriesDeferred = async { catalogRepository.getCategories() }
            val coursesDeferred = async { catalogRepository.getCourses(idCategory) }

            val categoriesResult = categoriesDeferred.await()
            val coursesResult = coursesDeferred.await()

            categoriesResult.onSuccess { categories ->
                val selectedCategory = categories.find { it.idCategory == idCategory }
                    ?: categories.firstOrNull()

                selectedCategory?.let { category ->
                    binding.tvRouteTitle.text = category.name
                    binding.tvRouteDescription.text = category.description
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
                "Curso seleccionado: ${selectedCourse.name}",
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

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.name
            binding.tvCourseSubtitle.text = course.description.ifEmpty {
                "+${course.rewardXp} XP al completar"
            }

            binding.root.setOnClickListener {
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
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}