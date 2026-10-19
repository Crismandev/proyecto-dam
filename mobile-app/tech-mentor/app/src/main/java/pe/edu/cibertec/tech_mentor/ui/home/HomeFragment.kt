package pe.edu.cibertec.tech_mentor.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.MainActivity
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentHomeBinding
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.util.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Vive en MainActivity: sobrevive al cambio de pestaña
    private val vm: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(requireContext().applicationContext)
    }

    /** idCurso del curso actualmente en progreso, se actualiza en updateObjectiveSection */
    private var currentCourseId: Long = -1L

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
        showFallbackGreeting()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { state ->
                    runCatching { render(state) }
                        .onFailure { error -> Log.e("HomeFragment", "render", error) }
                }
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
            (activity as? MainActivity)?.selectTab(R.id.practiceFragment)
        }

        binding.cvManageCourses.setOnClickListener {
            (activity as? MainActivity)?.selectTab(R.id.categoryFragment)
        }
        binding.cvManageMissions.setOnClickListener {
            (activity as? MainActivity)?.selectTab(R.id.categoryFragment)
        }

        binding.cvCurrentObjective.setOnClickListener {
            val bundle = bundleOf("idCurso" to currentCourseId)
            findNavController().navigate(
                R.id.action_homeFragment_to_courseDetailFragment,
                bundle
            )
        }
    }

    private fun showFallbackGreeting() {
        val session = SessionManager(requireContext())
        val fallback = session.getUsername()
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            ?: if (session.isStaff()) "Admin" else "Estudiante"
        binding.tvWelcomeTitle.text = "Un paso más, $fallback."
    }

    private fun render(s: HomeUiState) {
        binding.llStaffPanel.isVisible = s.isStaff
        binding.clObjectives.isVisible = !s.isStaff
        binding.cvDailyMission.isVisible = !s.isStaff
        binding.cvWeeklyChallenge.isVisible = !s.isStaff

        // Versión anterior (comentada): solo actualizaba el header si profile != null
        // → cuando la API falla, profile es null y el nombre nunca se mostraba
        // val profile = s.profile
        // if (profile != null) {
        //     val firstName = profile.nombres.orEmpty()...
        //     setupUserHeader(firstName, profile.rachaActual, profile.xpTotal)
        // }

        // Faltaba: actualizar header siempre, con fallback cuando profile es null
        val profile = s.profile
        val firstName = profile?.nombres.orEmpty().trim().substringBefore(" ").ifEmpty {
            if (s.isStaff) "Admin" else "Estudiante"
        }
        setupUserHeader(
            firstName = firstName,
            streakDays = profile?.rachaActual ?: 0,
            totalXp = profile?.xpTotal ?: 0
        )
        if (s.isStaff && profile != null) {
            binding.tvWelcomeSubtitle.text = getString(R.string.desc_staff_home)
        }

        if (s.isStaff) {
            val courses = s.courses.orEmpty()
            val missions = s.missions.orEmpty()
            binding.tvStaffCourseCount.text = getString(R.string.label_staff_courses_count, courses.size)
            binding.tvStaffMissionCount.text = getString(R.string.label_staff_missions_count, missions.size)
            binding.tvStaffMissionPreview.text = missions.take(4).joinToString("\n") { mission ->
                "• ${mission.nombre} (${mission.tipo})"
            }.ifBlank { "No hay misiones publicadas todavía." }
        } else {
            // Versión anterior (comentada): solo llamaba updateObjectiveSection si ambos != null
            // → cuando la API falla, progress y courses quedan null y la sección nunca se dibujaba
            // val courses = s.courses
            // val progress = s.progress
            // if (courses != null && progress != null) { updateObjectiveSection(progress, courses) }

            // Faltaba: siempre llamar updateObjectiveSection, usando listas vacías como fallback
            updateObjectiveSection(
                progressList = s.progress.orEmpty(),
                allCourses = s.courses.orEmpty()
            )
        }

        // Versión anterior (comentada): solo llamaba updateMissionsSection si alguno != null
        // → si ambos eran null (API caída), la sección de misiones no se renderizaba
        // if (s.userMissions != null || s.missions != null) {
        //     updateMissionsSection(s.userMissions.orEmpty(), s.missions.orEmpty())
        // }

        // Faltaba: siempre renderizar la sección de misiones con fallback vacío
        updateMissionsSection(s.userMissions.orEmpty(), s.missions.orEmpty())
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

        // Guardamos el id del curso para usarlo en el click de cvCurrentObjective
        currentCourseId = recentProgress?.idCurso ?: course?.idCurso ?: -1L

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