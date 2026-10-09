package pe.edu.cibertec.techmentor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class LeccionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_leccion,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val tvTituloLeccion =
            view.findViewById<TextView>(R.id.tvTituloLeccion)

        val tvProgresoLeccion =
            view.findViewById<TextView>(R.id.tvProgresoLeccion)

        val progresoLeccion =
            view.findViewById<ProgressBar>(R.id.progresoLeccion)

        val tvContenidoLeccion =
            view.findViewById<TextView>(R.id.tvContenidoLeccion)

        val tvEjemploLeccion =
            view.findViewById<TextView>(R.id.tvEjemploLeccion)

        val tvRecordarLeccion =
            view.findViewById<TextView>(R.id.tvRecordarLeccion)

        val btnCompletarLeccion =
            view.findViewById<Button>(R.id.btnCompletarLeccion)

        val leccionId =
            arguments?.getInt("leccionId", 1) ?: 1

        val leccionNombre =
            arguments?.getString("leccionNombre")
                ?: "Lección"

        val cursoNombre =
            arguments?.getString("cursoNombre")
                ?: "Curso"

        // Mostrar información de la lección

        tvTituloLeccion.text = leccionNombre

        tvProgresoLeccion.text =
            "Lección $leccionId de 4"

        progresoLeccion.progress =
            (leccionId * 25).coerceAtMost(100)

        // Contenido

        tvContenidoLeccion.text = """
            Una API es un mecanismo que permite que diferentes aplicaciones puedan comunicarse entre sí.

            En el desarrollo de software, una API permite que una aplicación solicite información o ejecute determinadas operaciones en otro sistema.

            En este curso aprenderás a trabajar con APIs utilizando Spring Boot.

            Antes de comenzar a desarrollar una API, es importante comprender conceptos como solicitudes, respuestas, métodos HTTP y formato JSON.
        """.trimIndent()

        tvEjemploLeccion.text = """
            Imagina que una aplicación necesita consultar los datos de un usuario.

            La aplicación realiza una solicitud a una API y esta procesa la petición.

            Finalmente, la API devuelve una respuesta con la información solicitada.
        """.trimIndent()

        tvRecordarLeccion.text =
            "Una API permite la comunicación entre diferentes aplicaciones o sistemas."

        // Guardar progreso

        val preferencias =
            requireContext().getSharedPreferences(
                "progreso_techmentor",
                Context.MODE_PRIVATE
            )

        val claveLeccion =
            "${cursoNombre}_basico_leccion_$leccionId"

        val completada =
            preferencias.getBoolean(claveLeccion, false)

        if (completada) {
            btnCompletarLeccion.text =
                "✓ LECCIÓN COMPLETADA"

            btnCompletarLeccion.isEnabled = false
        }

        btnCompletarLeccion.setOnClickListener {

            preferencias.edit()
                .putBoolean(claveLeccion, true)
                .apply()

            btnCompletarLeccion.text =
                "✓ LECCIÓN COMPLETADA"

            btnCompletarLeccion.isEnabled = false

            // Volvemos a la pantalla del nivel

            parentFragmentManager.popBackStack()
        }
    }
}