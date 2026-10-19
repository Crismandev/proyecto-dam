package pe.edu.cibertec.tech_mentor.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentHomeBinding
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.model.UserMission

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Vive en MainActivity: sobrevive al cambio de pestaña
    private val vm: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(requireContext().applicationContext)
    }

    companion object {
        private const val DISCORD_INVITE_URL = "https://discord.gg/techmentor"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { render(it) }
            }
        }
        vm.refresh()
    }

    private fun setupClickListeners() {
        binding.cvCommunity.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DISCORD_INVITE_URL))
            startActivity(intent)
        }

        binding.cvPractice.setOnClickListener {
            navigateToPractice()
        }
    }

    private fun navigateToPractice() {
        runCatching {
            findNavController().navigate(R.id.practiceFragment)
        }.onFailure {
            Toast.makeText(requireContext(), "Verifica el ID de PracticeFragment en tu nav_graph", Toast.LENGTH_SHORT).show()
        }
    }

    private fun render(s: HomeUiState) {
        val profile = s.profile
        if (profile != null) {
            val firstName = profile.nombres.trim().substringBefore(" ").ifEmpty { "Estudiante" }
            setupUserHeader(
                firstName = firstName,
                streakDays = profile.rachaActual,
                totalXp = profile.xpTotal
            )
        }

        val courses = s.courses
        val progress = s.progress
        if (courses != null && progress != null) {
            updateObjectiveSection(progress, courses)
        }

        val userMissions = s.userMissions
        if (userMissions != null) {
            updateMissionsSection(userMissions, s.missions.orEmpty())
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
        val sortedProgress = progressList.sortedWith(
            compareByDescending<CourseProgress> { it.fechaUltimaActividad ?: it.fechaInicio ?: "" }
                .thenByDescending { it.idProgreso }
        )

        val recentProgress = sortedProgress.find {
            it.estado.equals("EN_CURSO", ignoreCase = true) ||
                    it.estado.equals("EN_PROGRESO", ignoreCase = true)
        } ?: sortedProgress.firstOrNull()

        val course = allCourses.find { it.idCurso == recentProgress?.idCurso }
            ?: allCourses.firstOrNull()

        val currentLevelName = recentProgress?.nombreNivelActual ?: "Básico"

        binding.tvObjectiveType.text = "Ruta"
        binding.tvObjectiveLevel.text = currentLevelName
        updateLevelIcon(currentLevelName)

        binding.tvObjectiveCourse.text = recentProgress?.nombreCurso?.takeIf { it.isNotBlank() }
            ?: course?.nombre
                    ?: "Explora nuevos cursos"

        val categoryId = course?.idCategoria
        val totalCategoryCourses = if (categoryId != null) {
            allCourses.count { it.idCategoria == categoryId }.coerceAtLeast(1)
        } else {
            allCourses.size.coerceAtLeast(1)
        }

        val completedCoursesCount = if (categoryId != null) {
            progressList.count { progress ->
                progress.estado.equals("COMPLETADO", ignoreCase = true) &&
                        allCourses.any { c -> c.idCurso == progress.idCurso && c.idCategoria == categoryId }
            }
        } else {
            progressList.count { it.estado.equals("COMPLETADO", ignoreCase = true) }
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

    private fun updateMissionsSection(
        userMissions: List<UserMission>,
        catalogMissions: List<Mission>
    ) {
        val missionsById = catalogMissions.associateBy { it.idMision }

        val dailyUserMission = userMissions.find {
            missionsById[it.idMision]?.tipo?.contains("DIARIA", ignoreCase = true) == true
        } ?: userMissions.firstOrNull { it.nombreMision.contains("diaria", ignoreCase = true) }

        val dailyCatalogMission = catalogMissions.find { it.tipo.contains("DIARIA", ignoreCase = true) }

        if (dailyUserMission != null) {
            val missionInfo = missionsById[dailyUserMission.idMision]
            val goal = dailyUserMission.meta.coerceAtLeast(1)
            val progress = dailyUserMission.progresoActual.coerceAtMost(goal)

            binding.tvMissionTitle.text = dailyUserMission.nombreMision.ifBlank {
                missionInfo?.nombre ?: "Tu pequeño logro de hoy"
            }
            binding.tvMissionSubtitle.text = missionInfo?.descripcion ?: "Completa tus lecciones"
            binding.pbMission.max = goal
            binding.pbMission.progress = progress
            binding.tvMissionProgressText.text = "$progress/$goal"
        } else if (dailyCatalogMission != null) {
            val goal = dailyCatalogMission.meta.coerceAtLeast(1)
            binding.tvMissionTitle.text = dailyCatalogMission.nombre
            binding.tvMissionSubtitle.text = dailyCatalogMission.descripcion
            binding.pbMission.max = goal
            binding.pbMission.progress = 0
            binding.tvMissionProgressText.text = "0/$goal"
        }

        val weeklyUserMission = userMissions.find {
            it.idMisionUsuario != dailyUserMission?.idMisionUsuario &&
                    missionsById[it.idMision]?.tipo?.contains("SEMANAL", ignoreCase = true) == true
        }
        val weeklyCatalogMission = catalogMissions.find { it.tipo.contains("SEMANAL", ignoreCase = true) }

        if (weeklyUserMission != null) {
            val missionInfo = missionsById[weeklyUserMission.idMision]
            val goal = weeklyUserMission.meta.coerceAtLeast(1)
            val progress = weeklyUserMission.progresoActual.coerceAtMost(goal)

            binding.tvChallengeTitle.text = weeklyUserMission.nombreMision.ifBlank {
                missionInfo?.nombre ?: "Desafío de la semana"
            }
            binding.tvChallengeSubtitle.text = missionInfo?.descripcion ?: "Gana experiencia"
            binding.pbChallenge.max = goal
            binding.pbChallenge.progress = progress
            binding.tvChallengeProgressText.text = "$progress/$goal XP"
        } else if (weeklyCatalogMission != null) {
            val goal = weeklyCatalogMission.meta.coerceAtLeast(1)
            binding.tvChallengeTitle.text = weeklyCatalogMission.nombre
            binding.tvChallengeSubtitle.text = weeklyCatalogMission.descripcion
            binding.pbChallenge.max = goal
            binding.pbChallenge.progress = 0
            binding.tvChallengeProgressText.text = "0/$goal XP"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}