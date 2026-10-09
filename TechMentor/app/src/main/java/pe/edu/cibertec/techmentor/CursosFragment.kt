package pe.edu.cibertec.techmentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class CursosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_cursos,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val btnSeleccionCurso =
            view.findViewById<Button>(R.id.btnSeleccionCurso)

        val btnEvaluacionDiagnostica =
            view.findViewById<Button>(R.id.btnEvaluacionDiagnostica)

        // SELECCIONAR CURSO
        btnSeleccionCurso.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    ListaCursosFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        // EVALUACIÓN DIAGNÓSTICA
        btnEvaluacionDiagnostica.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Aquí comenzará la evaluación de nivel",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}