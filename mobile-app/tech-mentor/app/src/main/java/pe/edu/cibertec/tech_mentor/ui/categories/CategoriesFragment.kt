package pe.edu.cibertec.tech_mentor.ui.categories

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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentCategoriesBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCategoryBinding
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.isSuccess
import pe.edu.cibertec.tech_mentor.data.remote.getOrDefault

data class CourseCategory(
    val idCategory: Long,
    val title: String,
    val coursesCount: Int
)

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRepository: CatalogRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRepository()
        loadCategories()
    }

    private fun setupRepository() {
        // --- Versión anterior simulada (comentada): ---
        // catalogRepository = FakeCatalogRepository()

        // --- Versión real conectada a la API de TechMentor: ---
        val sessionManager = SessionManager(requireContext())
        val catalogApi = ApiClient.getClient(sessionManager).create(CatalogApiService::class.java)
        catalogRepository = CatalogRepositoryImpl(catalogApi)
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            val categoriesDeferred = async { catalogRepository.getCategories() }
            val coursesDeferred = async { catalogRepository.getCourses() }

            val categoriesResult = categoriesDeferred.await()
            val coursesResult = coursesDeferred.await()

            if (categoriesResult.isSuccess && coursesResult.isSuccess) {
                val apiCategories = categoriesResult.getOrDefault(emptyList())
                val apiCourses = coursesResult.getOrDefault(emptyList())

                val displayCategories = apiCategories.map { category ->
                    // --- Versión anterior con nombres en inglés (comentada): ---
                    // val courseCount = apiCourses.count { it.idCategory == category.idCategory }
                    // CourseCategory(idCategory = category.idCategory, title = category.name, coursesCount = courseCount)

                    // --- Propiedades del modelo de dominio TechMentor (idCategoria, nombre): ---
                    val courseCount = apiCourses.count { it.idCategoria == category.idCategoria }

                    CourseCategory(
                        idCategory = category.idCategoria,
                        title = category.nombre,
                        coursesCount = courseCount
                    )
                }

                setupRecyclerView(displayCategories)
            } else {
                Toast.makeText(requireContext(), "Error al cargar las rutas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(categories: List<CourseCategory>) {
        val adapter = CourseCategoryAdapter(categories) { selectedCategoryId ->

            val bundle = bundleOf("idCategory" to selectedCategoryId)

            findNavController().navigate(
                R.id.action_categoryFragment_to_categoryDetailsFragment,
                bundle
            )
        }
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourses.adapter = adapter
    }
}

class CourseCategoryAdapter(
    private val categories: List<CourseCategory>,
    private val onCategoryClick: (Long) -> Unit
) : RecyclerView.Adapter<CourseCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CourseCategory) {
            binding.tvCategoryTitle.text = category.title
            binding.tvCategorySubtitle.text = "${category.coursesCount} Cursos"

            binding.root.setOnClickListener {
                onCategoryClick(category.idCategory)
            }
            binding.btnVer.setOnClickListener {
                onCategoryClick(category.idCategory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}