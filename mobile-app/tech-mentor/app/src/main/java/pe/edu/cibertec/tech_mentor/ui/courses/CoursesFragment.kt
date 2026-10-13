package pe.edu.cibertec.tech_mentor.ui.courses

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.databinding.FragmentCoursesBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCourseCategoryBinding

data class CourseCategory(
    val title: String,
    val coursesCount: Int,
    val iconColorHex: String,
    val bgColorHex: String
)

class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val fakeData = listOf(
            CourseCategory("Desarrollo Web", 16, "#198754", "#E8F5E9"), // Verde
            CourseCategory("English Academy", 7, "#D81B60", "#FCE4EC"), // Rosa
            CourseCategory("Marketing Digital", 15, "#0D6EFD", "#E3F2FD"), // Azul
            CourseCategory("Data Science", 12, "#6F42C1", "#F4F0FF"), // Morado
            CourseCategory("Diseño UX/UI", 9, "#FD7E14", "#FFF3E0"), // Naranja
            CourseCategory("Desarrollo Móvil", 11, "#20C997", "#E6F8F3"), // Verde agua
            CourseCategory("Ciberseguridad", 5, "#DC3545", "#FFEBEE"), // Rojo
            CourseCategory("Cloud Computing", 8, "#0DCAF0", "#E0F7FA")  // Cian
        )

        val adapter = CourseCategoryAdapter(fakeData)
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourses.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CourseCategoryAdapter(private val categories: List<CourseCategory>) :
    RecyclerView.Adapter<CourseCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCourseCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CourseCategory) {
            binding.tvCategoryTitle.text = category.title

            binding.tvCategorySubtitle.text = "${category.coursesCount} Cursos"

            binding.ivCategoryIcon.setColorFilter(Color.parseColor(category.iconColorHex))
            binding.cvIconContainer.setCardBackgroundColor(Color.parseColor(category.bgColorHex))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCourseCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}