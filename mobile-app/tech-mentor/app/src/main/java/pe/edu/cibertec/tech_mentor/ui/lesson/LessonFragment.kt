package pe.edu.cibertec.tech_mentor.ui.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pe.edu.cibertec.tech_mentor.databinding.FragmentLessonBinding

class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLessonData()
        setupListeners()
    }

    private fun setupLessonData() {
        binding.tvLessonOrder.text = "LECCIÓN 1"

        binding.tvLessonTitle.text = "Introducción a las Variables"

        binding.tvLessonDescription.text = "En esta lección aprenderemos qué son las variables, cómo declararlas y los tipos de datos básicos que puedes almacenar en ellas."

        val contenidoSimulado = """
            Imagina que una variable es como una caja vacía donde puedes guardar cosas. Para poder encontrar esa caja más tarde, necesitas ponerle una etiqueta con un nombre.
            
            En programación, usamos variables para guardar información temporalmente mientras nuestro programa se está ejecutando. Por ejemplo, si estás haciendo un juego, podrías usar una variable para guardar la puntuación del jugador.
            
            Para crear (o 'declarar') una variable, primero debes decirle a la computadora qué tipo de caja necesitas y luego ponerle un nombre. 
            
            Existen diferentes tipos de cajas para diferentes tipos de información. Si quieres guardar un número, usarás un tipo de variable; si quieres guardar una palabra, usarás otro.
        """.trimIndent()

        binding.tvLessonContent.text = contenidoSimulado
    }

    private fun setupListeners() {
        binding.btnSolveExercises.setOnClickListener {
            Toast.makeText(requireContext(), "¡Pronto podrás resolver los ejercicios de esta lección!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}