package pe.edu.cibertec.tech_mentor.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.getOrDefault
import pe.edu.cibertec.tech_mentor.data.remote.isSuccess
import pe.edu.cibertec.tech_mentor.data.remote.onFailure
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.repository.UserRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui.asStateFlow()

    private var refreshJob: Job? = null

    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            val profile = async { userRepository.getMyProfile() }
            val professions = async { userRepository.getMyProfessions() }
            val progress = async { progressRepository.getMyCoursesProgress() }
            val courses = async { catalogRepository.getCourses() }
            val categories = async { catalogRepository.getCategories() }

            profile.await()
                .onSuccess { user -> _ui.update { s -> s.copy(user = user) } }
                .onFailure { error -> Log.w(TAG, "perfil: ${error.message}") }

            professions.await().onSuccess { list ->
                val main = list.find { it.principal } ?: list.firstOrNull()
                _ui.update { s -> s.copy(profession = main?.nombreProfesion) }
            }

            val progressResult = progress.await()
            val coursesResult = courses.await()
            val categoriesResult = categories.await()

            if (progressResult.isSuccess && coursesResult.isSuccess && categoriesResult.isSuccess) {
                val allCourses = coursesResult.getOrDefault(emptyList())
                val allCategories = categoriesResult.getOrDefault(emptyList())
                val completedProgress = progressResult.getOrDefault(emptyList())
                    .filter { it.estado.equals("COMPLETADO", ignoreCase = true) }

                val completedCourseIds = completedProgress.map { it.idCurso }.toSet()

                val completedCourses = completedProgress.map { p ->
                    p.nombreCurso?.takeIf { it.isNotBlank() }
                        ?: allCourses.find { it.idCurso == p.idCurso }?.nombre
                        ?: "Curso completado"
                }

                val completedRoutes = allCategories.filter { category ->
                    val categoryCourses = allCourses.filter { it.idCategoria == category.idCategoria }
                    categoryCourses.isNotEmpty() && categoryCourses.all { it.idCurso in completedCourseIds }
                }.map { it.nombre }

                _ui.update { s ->
                    s.copy(completedCourses = completedCourses, completedRoutes = completedRoutes)
                }
            } else {
                Log.w(TAG, "no se pudo cargar el progreso")
            }
        }
    }

    class Factory(private val appContext: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val client = ApiClient.getClient(SessionManager(appContext))
            return ProfileViewModel(
                UserRepositoryImpl(client.create(UserApiService::class.java)),
                ProgressRepositoryImpl(client.create(ProgressApiService::class.java)),
                CatalogRepositoryImpl(client.create(CatalogApiService::class.java))
            ) as T
        }
    }

    private companion object {
        const val TAG = "ProfileViewModel"
    }
}