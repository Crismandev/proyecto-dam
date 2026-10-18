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
        // Datos simulados
        catalogRepository = FakeCatalogRepository()
        gamificationRepository = FakeGamificationRepository()
        progressRepository = FakeProgressRepository()

        // Descomentar cuando el backend esté listo
        // val context = requireContext()
        // catalogRepository = CatalogRepositoryImpl(RetrofitClient.getCatalogApi(context))
        // gamificationRepository = GamificationRepositoryImpl(RetrofitClient.getGamificationApi(context))
        // progressRepository = ProgressRepositoryImpl(RetrofitClient.getProgressApi(context))
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
            it.status.equals("EN_PROGRESO", ignoreCase = true)
        } ?: progressList.firstOrNull()

        val course = activeProgress?.course
            ?: allCourses.find { it.idCourse == activeProgress?.idCourse }
            ?: allCourses.firstOrNull()

        val currentLevelName = activeProgress?.currentLevel?.name ?: "Básico"

        binding.tvObjectiveType.text = "Ruta"
        binding.tvObjectiveLevel.text = currentLevelName
        updateLevelIcon(currentLevelName)

        binding.tvObjectiveCourse.text = course?.name ?: "Explora nuevos cursos"

        val categoryId = course?.idCategory
        val totalCategoryCourses = if (categoryId != null) {
            allCourses.count { it.idCategory == categoryId }.coerceAtLeast(1)
        } else {
            allCourses.size.coerceAtLeast(1)
        }

        val completedCoursesCount = progressList.count {
            it.status.equals("COMPLETADO", ignoreCase = true)
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
        val dailyMission = userMissions.find {
            it.mission?.type?.equals("DIARIA", ignoreCase = true) == true
        } ?: userMissions.getOrNull(0)

        dailyMission?.let { userMission ->
            val mission = userMission.mission
            val goal = (mission?.goal ?: 2).coerceAtLeast(1)
            val progress = userMission.currentProgress.coerceAtMost(goal)

            binding.tvMissionTitle.text = mission?.name
            binding.tvMissionSubtitle.text = mission?.description
            binding.pbMission.max = goal
            binding.pbMission.progress = progress
            binding.tvMissionProgressText.text = "$progress/$goal"
        }

        // Desafío Semanal
        val weeklyChallenge = userMissions.find {
            it.mission?.type?.equals("SEMANAL", ignoreCase = true) == true
        } ?: userMissions.getOrNull(1)

        weeklyChallenge?.let { userMission ->
            val mission = userMission.mission
            val goal = (mission?.goal ?: 500).coerceAtLeast(1)
            val progress = userMission.currentProgress.coerceAtMost(goal)

            binding.tvChallengeTitle.text = mission?.name
            binding.tvChallengeSubtitle.text = mission?.description
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