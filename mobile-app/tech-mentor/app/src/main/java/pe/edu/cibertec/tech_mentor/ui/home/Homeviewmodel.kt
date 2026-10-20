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
import kotlinx.coroutines.supervisorScope
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
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
// Faltaba: import de TechMentorDbHelper para persistencia SQLite de inicio
import pe.edu.cibertec.tech_mentor.data.local.TechMentorDbHelper

// Versión anterior (comentada): no recibía dbHelper para persistencia local
/*
class HomeViewModel(
    private val userRepository: UserRepository,
    private val catalogRepository: CatalogRepository,
    private val gamificationRepository: GamificationRepository,
    private val progressRepository: ProgressRepository,
    isStaff: Boolean
) : ViewModel()
*/

// Faltaba: soporte de persistencia local en SQLite para Inicio (cursos y perfil de usuario)
class HomeViewModel(
    private val userRepository: UserRepository,
    private val catalogRepository: CatalogRepository,
    private val gamificationRepository: GamificationRepository,
    private val progressRepository: ProgressRepository,
    isStaff: Boolean,
    private val dbHelper: TechMentorDbHelper? = null
) : ViewModel() {

    private val _ui = MutableStateFlow(HomeUiState(isStaff = isStaff))
    val ui: StateFlow<HomeUiState> = _ui.asStateFlow()

    private var refreshJob: Job? = null

    init {
        // Faltaba: precargar datos de inicio persistidos en SQLite para respuesta instantánea offline
        dbHelper?.let { db ->
            val cachedProfile = db.getProfile()
            val cachedCourses = db.getCourses()
            if (cachedProfile != null || cachedCourses.isNotEmpty()) {
                _ui.update { s ->
                    s.copy(
                        profile = cachedProfile ?: s.profile,
                        courses = if (cachedCourses.isNotEmpty()) cachedCourses else s.courses
                    )
                }
            }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        val staff = _ui.value.isStaff

        refreshJob = viewModelScope.launch {
            supervisorScope {
                val profile = async { userRepository.getMyProfile() }
                val courses = async { catalogRepository.getCourses() }
                val missions = async { gamificationRepository.getMissions() }
                val progress = async {
                    if (staff) ApiResponse.Success(emptyList())
                    else progressRepository.getMyCoursesProgress()
                }
                val myMissions = async {
                    if (staff) ApiResponse.Success(emptyList())
                    else gamificationRepository.getMyMissions()
                }

                profile.await()
                    .onSuccess { p ->
                        // Faltaba: guardar perfil actualizado en SQLite
                        dbHelper?.saveProfile(p)
                        _ui.update { s -> s.copy(profile = p) }
                    }
                    .onFailure { error ->
                        Log.w(TAG, "perfil: ${error.message}")
                        // Faltaba: fallback a SQLite si la red falla
                        val cachedProfile = dbHelper?.getProfile()
                        if (cachedProfile != null && _ui.value.profile == null) {
                            _ui.update { s -> s.copy(profile = cachedProfile) }
                        }
                    }

                courses.await()
                    .onSuccess { list ->
                        // Faltaba: guardar cursos en SQLite para persistencia local de inicio
                        dbHelper?.saveCourses(list)
                        _ui.update { s -> s.copy(courses = list) }
                    }
                    .onFailure { error ->
                        Log.w(TAG, "cursos: ${error.message}")
                        // Faltaba: fallback a cursos de SQLite si la API no responde
                        val cachedCourses = dbHelper?.getCourses().orEmpty()
                        _ui.update { s -> s.copy(courses = if (cachedCourses.isNotEmpty()) cachedCourses else emptyList()) }
                    }

                progress.await()
                    .onSuccess { list -> _ui.update { s -> s.copy(progress = list) } }
                    .onFailure { error ->
                        _ui.update { s -> s.copy(progress = emptyList()) }
                        Log.w(TAG, "progreso: ${error.message}")
                    }

                missions.await()
                    .onSuccess { list -> _ui.update { s -> s.copy(missions = list) } }
                    .onFailure { error ->
                        _ui.update { s -> s.copy(missions = emptyList()) }
                        Log.w(TAG, "misiones: ${error.message}")
                    }

                myMissions.await()
                    .onSuccess { list -> _ui.update { s -> s.copy(userMissions = list) } }
                    .onFailure { error ->
                        _ui.update { s -> s.copy(userMissions = emptyList()) }
                        Log.w(TAG, "mis misiones: ${error.message}")
                    }
            }
        }
    }

    class Factory(private val appContext: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val session = SessionManager(appContext)
            val client = ApiClient.getClient(session)
            // Faltaba: inyectar TechMentorDbHelper a la fábrica de HomeViewModel
            val dbHelper = TechMentorDbHelper(appContext)
            return HomeViewModel(
                UserRepositoryImpl(client.create(UserApiService::class.java)),
                CatalogRepositoryImpl(client.create(CatalogApiService::class.java)),
                GamificationRepositoryImpl(client.create(GamificationApiService::class.java)),
                ProgressRepositoryImpl(client.create(ProgressApiService::class.java)),
                session.isStaff(),
                dbHelper
            ) as T
        }
    }

    private companion object {
        const val TAG = "HomeViewModel"
    }
}
