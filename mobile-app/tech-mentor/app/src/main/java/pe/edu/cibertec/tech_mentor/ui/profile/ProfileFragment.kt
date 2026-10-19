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
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository
import pe.edu.cibertec.tech_mentor.data.repository.UserRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.isSuccess
import pe.edu.cibertec.tech_mentor.data.remote.getOrDefault
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var progressRepository: ProgressRepository
    private lateinit var catalogRepository: CatalogRepository
    private lateinit var userRepository: UserRepository

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
        // --- Versión anterior simulada (comentada): ---
        // progressRepository = FakeProgressRepository()
        // catalogRepository = FakeCatalogRepository()

        // --- Versión real conectada a la API de TechMentor: ---
        val client = ApiClient.getClient(sessionManager)
        progressRepository = ProgressRepositoryImpl(client.create(ProgressApiService::class.java))
        catalogRepository = CatalogRepositoryImpl(client.create(CatalogApiService::class.java))
        userRepository = UserRepositoryImpl(client.create(UserApiService::class.java))
    }

    private fun setupListeners() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
    }

    private fun loadUserProfile() {
        val username = sessionManager.getUsername() ?: "Estudiante"
        val role = sessionManager.getUserRole() ?: "ALUMNO"

        binding.tvFullName.text = username
        binding.tvEmail.text = if (username.contains("@")) username else "$username@techmentor.com"
        binding.tvProfession.text = role
        binding.tvUserLevel.text = "Nvl. 1"
        binding.tvCurrentStreak.text = "7 días"
        binding.tvMaxStreak.text = "14 días"
        binding.tvTotalXp.text = "240 XP"
        binding.tvCoins.text = "50"

        viewLifecycleOwner.lifecycleScope.launch {
            userRepository.getMyProfile().onSuccess { user ->
                binding.tvFullName.text = "${user.nombres} ${user.apellidos}".trim()
                binding.tvEmail.text = user.email
                binding.tvBio.text = user.biografia.orEmpty()
                binding.tvPhone.text = user.telefono.orEmpty()
                binding.tvCity.text = user.ciudad.orEmpty()
                binding.tvUserLevel.text = "Nvl. ${user.nivelUsuario}"
                binding.tvCurrentStreak.text = "${user.rachaActual} días"
                binding.tvMaxStreak.text = "${user.rachaMaxima} días"
                binding.tvTotalXp.text = "${user.xpTotal} XP"
                binding.tvCoins.text = user.monedas.toString()

                val avatarUrl = user.avatarUrl
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this@ProfileFragment)
                        .load(avatarUrl)
                        .into(binding.ivAvatar)
                }
            }
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
                    .filter { it.estado.equals("COMPLETADO", ignoreCase = true) }

                val completedCourseIds = completedProgress.map { it.idCurso }.toSet()

                completedCourses = completedProgress.map { progress ->
                    progress.nombreCurso.ifEmpty {
                        allCourses.find { it.idCurso == progress.idCurso }?.nombre ?: "Curso completado"
                    }
                }

                completedRoutes = allCategories.filter { category ->
                    val categoryCourses = allCourses.filter { it.idCategoria == category.idCategoria }
                    categoryCourses.isNotEmpty() && categoryCourses.all { it.idCurso in completedCourseIds }
                }.map { it.nombre }

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