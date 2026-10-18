package pe.edu.cibertec.tech_mentor.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentProfileBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCompletedCourseBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var progressRepository: ProgressRepository
    private lateinit var catalogRepository: CatalogRepository

    private var completedCourses = listOf<String>()
    private var completedRoutes = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        setupRepositories()
        setupListeners()
        loadUserProfile()
        loadCompletedCoursesAndRoutes()
    }

    private fun setupRepositories() {
        // MODO PRUEBA (Datos desde los FakeRepository)
        progressRepository = FakeProgressRepository()
        catalogRepository = FakeCatalogRepository()

        // MODO REAL (Descomentar cuando el backend esté listo):
        // val context = requireContext()
        // progressRepository = ProgressRepositoryImpl(RetrofitClient.getProgressApi(context))
        // catalogRepository = CatalogRepositoryImpl(RetrofitClient.getCatalogApi(context))
    }

    private fun setupListeners() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
    }

    private fun loadUserProfile() {
        val user = sessionManager.getUser() ?: return

        binding.tvFullName.text = "${user.firstName} ${user.lastName.orEmpty()}".trim()
        binding.tvEmail.text = user.email
        binding.tvBio.text = user.biography.orEmpty()
        binding.tvPhone.text = user.phone.orEmpty()
        binding.tvCity.text = user.city.orEmpty()
        binding.tvProfession.text = user.profession.orEmpty()
        binding.tvUserLevel.text = "Nvl. ${user.level}"

        binding.tvCurrentStreak.text = "${user.currentStreak} días"
        binding.tvMaxStreak.text = "${user.maxStreak} días"
        binding.tvTotalXp.text = "${user.totalXp} XP"
        binding.tvCoins.text = user.coins.toString()

        val avatarUrl = user.avatar
        if (!avatarUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatarUrl)
                .into(binding.ivAvatar)
        }
    }

    private fun loadCompletedCoursesAndRoutes() {
        viewLifecycleOwner.lifecycleScope.launch {
            val progressDeferred = async { progressRepository.getMyCoursesProgress() }
            val coursesDeferred = async { catalogRepository.getCourses() }
            val categoriesDeferred = async { catalogRepository.getCategories() }

            val progressResult = progressDeferred.await()
            val coursesResult = coursesDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            if (progressResult.isSuccess && coursesResult.isSuccess && categoriesResult.isSuccess) {
                val allCourses = coursesResult.getOrDefault(emptyList())
                val allCategories = categoriesResult.getOrDefault(emptyList())
                val completedProgress = progressResult.getOrDefault(emptyList())
                    .filter { it.status.equals("COMPLETADO", ignoreCase = true) }

                val completedCourseIds = completedProgress.map { it.idCourse }.toSet()

                completedCourses = completedProgress.mapNotNull { progress ->
                    progress.course?.name ?: allCourses.find { it.idCourse == progress.idCourse }?.name
                }

                completedRoutes = allCategories.filter { category ->
                    val categoryCourses = allCourses.filter { it.idCategory == category.idCategory }
                    categoryCourses.isNotEmpty() && categoryCourses.all { it.idCourse in completedCourseIds }
                }.map { it.name }

                setupCertificatesSection()
            } else {
                Toast.makeText(requireContext(), "Error al cargar el progreso", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCertificatesSection() {
        val adapter = CompletedAdapter(completedCourses)
        binding.rvCompletedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompletedItems.adapter = adapter

        binding.tabLayoutCompleted.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> adapter.updateList(completedCourses)
                    1 -> adapter.updateList(completedRoutes)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CompletedAdapter(private var items: List<String>) :
    RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    inner class CompletedViewHolder(val binding: ItemCompletedCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.tvCompletedTitle.text = title
        }
    }

    fun updateList(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val binding = ItemCompletedCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CompletedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}