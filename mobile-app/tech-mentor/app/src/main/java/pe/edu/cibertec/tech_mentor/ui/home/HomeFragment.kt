package pe.edu.cibertec.tech_mentor.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMockData()
    }

    private fun setupMockData() {
        binding.tvWelcomeTitle.text = "Un paso más, Luis."
        binding.tvStreak.text = "7 días de racha"
        binding.tvXp.text = "240 XP"

        val nivelActual = "Básico"
        binding.tvObjectiveType.text = "Ruta"
        binding.tvObjectiveLevel.text = nivelActual

// Versión anterior conservada; sustituida por el bloque activo que sigue.
//         when (nivelActual.lowercase()) {
//             "básico" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_code_purple)
//             "intermedio" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_code_purple)
//             "avanzado" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_code_purple)
//             else -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_code_purple)
//         }

        when (nivelActual.lowercase()) {
            "básico" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_cell_signal_low)
            "intermedio" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_cell_signal_medium)
            "avanzado" -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_cell_signal_high)
            else -> binding.ivObjectiveLevelIcon.setImageResource(R.drawable.ic_cell_signal_none)
        }

        binding.tvObjectiveCourse.text = "Desarrollo Frontend con JavaScript"

        binding.pbObjective.max = 12
        binding.pbObjective.progress = 5
        binding.tvObjectiveProgressText.text = "5/12 cursos"

        binding.tvMissionTitle.text = "Tu pequeño logro de hoy"
        binding.tvMissionSubtitle.text = "Completa una lección más."

        binding.pbMission.max = 2
        binding.pbMission.progress = 1
        binding.tvMissionProgressText.text = "1/2"

        binding.tvChallengeTitle.text = "Desafío de la semana"
        binding.tvChallengeSubtitle.text = "Acumula puntos para tu próxima medalla."

        binding.pbChallenge.max = 500
        binding.pbChallenge.progress = 150
        binding.tvChallengeProgressText.text = "150/500 XP"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}