package pe.edu.cibertec.tech_mentor.ui.home

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
import pe.edu.cibertec.tech_mentor.data.remote.api.GamificationApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.onFailure
import pe.edu.cibertec.tech_mentor.data.remote.onSuccess
import pe.edu.cibertec.tech_mentor.data.repository.GamificationRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.repository.ProgressRepositoryImpl
import pe.edu.cibertec.tech_mentor.data.repository.UserRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepositoryImpl
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

class HomeViewModel(
    private val userRepository: UserRepository,
    private val catalogRepository: CatalogRepository,
    private val gamificationRepository: GamificationRepository,
    private val progressRepository: ProgressRepository,
    isStaff: Boolean
) : ViewModel() {

    private val _ui = MutableStateFlow(HomeUiState(isStaff = isStaff))
    val ui: StateFlow<HomeUiState> = _ui.asStateFlow()

    private var refreshJob: Job? = null

    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            val profile = async { userRepository.getMyProfile() }
            val courses = async { catalogRepository.getCourses() }
            val progress = async { progressRepository.getMyCoursesProgress() }
            val missions = async { gamificationRepository.getMissions() }
            val myMissions = async { gamificationRepository.getMyMissions() }

            profile.await()
                .onSuccess { p -> _ui.update { s -> s.copy(profile = p) } }
                .onFailure { error -> Log.w(TAG, "perfil: ${error.message}") }

            courses.await()
                .onSuccess { list -> _ui.update { s -> s.copy(courses = list) } }
                .onFailure { error -> Log.w(TAG, "cursos: ${error.message}") }

            progress.await()
                .onSuccess { list -> _ui.update { s -> s.copy(progress = list) } }
                .onFailure { error -> Log.w(TAG, "progreso: ${error.message}") }

            missions.await()
                .onSuccess { list -> _ui.update { s -> s.copy(missions = list) } }
                .onFailure { error ->
                    _ui.update { s -> s.copy(missions = emptyList()) }
                    Log.w(TAG, "misiones: ${error.message}")
                }

            myMissions.await()
                .onSuccess { list -> _ui.update { s -> s.copy(userMissions = list) } }
                .onFailure { error ->
                    // 403 = cuenta staff (mis-misiones era solo ALUMNO/VIP); el catálogo igual se muestra.
                    _ui.update { s -> s.copy(userMissions = emptyList()) }
                    Log.w(TAG, "mis misiones: ${error.message}")
                }
        }
    }

    class Factory(private val appContext: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val session = SessionManager(appContext)
            val client = ApiClient.getClient(session)
            return HomeViewModel(
                UserRepositoryImpl(client.create(UserApiService::class.java)),
                CatalogRepositoryImpl(client.create(CatalogApiService::class.java)),
                GamificationRepositoryImpl(client.create(GamificationApiService::class.java)),
                ProgressRepositoryImpl(client.create(ProgressApiService::class.java)),
                session.isStaff()
            ) as T
        }
    }

    private companion object {
        const val TAG = "HomeViewModel"
    }
}