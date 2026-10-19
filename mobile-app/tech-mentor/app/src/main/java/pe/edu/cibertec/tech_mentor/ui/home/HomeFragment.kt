package pe.edu.cibertec.tech_mentor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentHomeBinding
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository
import pe.edu.cibertec.tech_mentor.data.repository.GamificationRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.GamificationApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.util.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.isSuccess
import pe.edu.cibertec.tech_mentor.data.remote.getOrDefault
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRepository: CatalogRepository
    private lateinit var gamificationRepository: GamificationRepository
    private lateinit var progressRepository: ProgressRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRepositories()
        loadHomeData()
    }

    private fun setupRepositories() {
        // --- Versión anterior simulada (comentada): ---
        // catalogRepository = FakeCatalogRepository()
        // gamificationRepository = FakeGamificationRepository()
        // progressRepository = FakeProgressRepository()

        // --- Versión real conectada a la API de TechMentor: ---
        val sessionManager = SessionManager(requireContext())
        val client = ApiClient.getClient(sessionManager)
        catalogRepository = CatalogRepositoryImpl(client.create(CatalogApiService::class.java))
        gamificationRepository = GamificationRepositoryImpl(client.create(GamificationApiService::class.java))
        progressRepository = ProgressRepositoryImpl(client.create(ProgressApiService::class.java))
    }

    private fun loadHomeData() {
        setupUserHeader(
            firstName = "Luis",
            streakDays = 7,
            totalXp = 240
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val coursesDeferred = async { catalogRepository.getCourses() }
            val progressDeferred = async { progressRepository.getMyCoursesProgress() }
            val missionsDeferred = async { gamificationRepository.getMyMissions() }

            val allCoursesResult = coursesDeferred.await()
            val myCoursesProgressResult = progressDeferred.await()
            val myMissionsResult = missionsDeferred.await()

            val allCourses = allCoursesResult.getOrDefault(emptyList())
            val myCoursesProgress = myCoursesProgressResult.getOrDefault(emptyList())

            updateObjectiveSection(myCoursesProgress, allCourses)

            myMissionsResult.onSuccess { userMissions ->
                updateMissionsSection(userMissions)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Error misiones: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupUserHeader(firstName: String, streakDays: Int, totalXp: Int) {
        binding.tvWelcomeTitle.text = "Un paso más, $firstName."
        binding.tvStreak.text = "$streakDays días de racha"
        binding.tvXp.text = "$totalXp XP"
    }

    private fun updateObjectiveSection(
        progressList: List<CourseProgress>,
        allCourses: List<Course>
    ) {
        val activeProgress = progressList.find {
            it.estado.equals("EN_PROGRESO", ignoreCase = true)
        } ?: progressList.firstOrNull()

        val course = allCourses.find { it.idCurso == activeProgress?.idCurso }
            ?: allCourses.firstOrNull()

        val currentLevelName = activeProgress?.nombreNivelActual ?: "Básico"

        binding.tvObjectiveType.text = "Ruta"
        binding.tvObjectiveLevel.text = currentLevelName
        updateLevelIcon(currentLevelName)

        binding.tvObjectiveCourse.text = course?.nombre ?: "Explora nuevos cursos"

        val categoryId = course?.idCategoria
        val totalCategoryCourses = if (categoryId != null) {
            allCourses.count { it.idCategoria == categoryId }.coerceAtLeast(1)
        } else {
            allCourses.size.coerceAtLeast(1)
        }

        val completedCoursesCount = progressList.count {
            it.estado.equals("COMPLETADO", ignoreCase = true)
        }

        binding.pbObjective.max = totalCategoryCourses
        binding.pbObjective.progress = completedCoursesCount.coerceAtMost(totalCategoryCourses)
        binding.tvObjectiveProgressText.text = "$completedCoursesCount/$totalCategoryCourses cursos"
    }

    private fun updateLevelIcon(levelName: String) {
        val normalized = levelName.lowercase()
        val iconRes = when {
            normalized.contains("básico") || normalized.contains("basico") -> R.drawable.ic_cell_signal_low
            normalized.contains("intermedio") -> R.drawable.ic_cell_signal_medium
            normalized.contains("avanzado") -> R.drawable.ic_cell_signal_high
            else -> R.drawable.ic_cell_signal_none
        }
        binding.ivObjectiveLevelIcon.setImageResource(iconRes)
    }

    private fun updateMissionsSection(userMissions: List<UserMission>) {
        // Misión Diaria
        val dailyMission = userMissions.getOrNull(0)
        dailyMission?.let { userMission ->
            val goal = userMission.meta.coerceAtLeast(1)
            val progress = userMission.progresoActual.coerceAtMost(goal)

            binding.tvMissionTitle.text = userMission.nombreMision
            binding.tvMissionSubtitle.text = "Completa tus lecciones del día"
            binding.pbMission.max = goal
            binding.pbMission.progress = progress
            binding.tvMissionProgressText.text = "$progress/$goal"
        }

        // Desafío Semanal
        val weeklyChallenge = userMissions.getOrNull(1)
        weeklyChallenge?.let { userMission ->
            val goal = userMission.meta.coerceAtLeast(1)
            val progress = userMission.progresoActual.coerceAtMost(goal)

            binding.tvChallengeTitle.text = userMission.nombreMision
            binding.tvChallengeSubtitle.text = "Gana experiencia acumulada"
            binding.pbChallenge.max = goal
            binding.pbChallenge.progress = progress
            binding.tvChallengeProgressText.text = "$progress/$goal XP"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}