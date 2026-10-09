package pe.edu.cibertec.techmentor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // ==========================================
        // BOTONES
        // ==========================================

        val btnContinuarCurso =
            view.findViewById<Button>(
                R.id.btnContinuarCurso
            )

        val btnVerLogros =
            view.findViewById<Button>(
                R.id.btnVerLogros
            )

        val tvCerrarSesion =
            view.findViewById<TextView>(
                R.id.tvCerrarSesion
            )

        // ==========================================
        // INFORMACIÓN DE "CONTINÚA TU CURSO"
        // ==========================================

        val tvCursoContinuar =
            view.findViewById<TextView>(
                R.id.tvCursoContinuar
            )

        val tvProgresoContinuar =
            view.findViewById<TextView>(
                R.id.tvProgresoContinuar
            )

        val tvSiguienteLeccion =
            view.findViewById<TextView>(
                R.id.tvSiguienteLeccion
            )

        val progresoContinuar =
            view.findViewById<ProgressBar>(
                R.id.progresoContinuar
            )

        // ==========================================
        // DATOS DEL CURSO
        // ==========================================

        val cursoNombre =
            "APIs con Spring Boot"

        val totalLecciones = 4

        val lecciones = listOf(
            Leccion(
                1,
                "Introducción a las APIs",
                "Conoce qué es una API y para qué sirve."
            ),
            Leccion(
                2,
                "Métodos HTTP",
                "Aprende GET, POST, PUT y DELETE."
            ),
            Leccion(
                3,
                "JSON",
                "Aprende cómo se estructura y utiliza JSON."
            ),
            Leccion(
                4,
                "Primer endpoint",
                "Crea tu primer endpoint utilizando Spring Boot."
            )
        )

        // ==========================================
        // CALCULAR PROGRESO
        // ==========================================

        val preferencias =
            requireContext().getSharedPreferences(
                "progreso_techmentor",
                Context.MODE_PRIVATE
            )

        var leccionesCompletadas = 0

        lecciones.forEach { leccion ->

            val claveLeccion =
                "${cursoNombre}_basico_leccion_${leccion.id}"

            if (
                preferencias.getBoolean(
                    claveLeccion,
                    false
                )
            ) {
                leccionesCompletadas++
            }
        }

        val porcentaje =
            (leccionesCompletadas * 100) /
                    totalLecciones

        // ==========================================
        // BUSCAR SIGUIENTE LECCIÓN
        // ==========================================

        val siguienteLeccion =
            lecciones.firstOrNull { leccion ->

                val claveLeccion =
                    "${cursoNombre}_basico_leccion_${leccion.id}"

                !preferencias.getBoolean(
                    claveLeccion,
                    false
                )
            }

        // ==========================================
        // MOSTRAR INFORMACIÓN EN HOME
        // ==========================================

        tvCursoContinuar.text =
            cursoNombre

        tvProgresoContinuar.text =
            "$porcentaje% completado"

        progresoContinuar.progress =
            porcentaje

        if (siguienteLeccion != null) {

            tvSiguienteLeccion.text =
                "Siguiente: ${siguienteLeccion.nombre}"

        } else {

            tvSiguienteLeccion.text =
                "🎉 ¡Nivel Básico completado!"

            btnContinuarCurso.text =
                "REVISAR LECCIONES"
        }

        // ==========================================
        // CONTINUAR CURSO
        // ==========================================

        btnContinuarCurso.setOnClickListener {

            val leccionDestino =
                siguienteLeccion
                    ?: lecciones.last()

            val fragment =
                LeccionFragment()

            val bundle =
                Bundle()

            bundle.putInt(
                "leccionId",
                leccionDestino.id
            )

            bundle.putString(
                "leccionNombre",
                leccionDestino.nombre
            )

            bundle.putString(
                "cursoNombre",
                cursoNombre
            )

            fragment.arguments =
                bundle

            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        }

        // ==========================================
        // GAMIFICACIÓN
        // ==========================================

        btnVerLogros.setOnClickListener {

            // Más adelante conectaremos con Gamificación.
            // Esta parte queda para tu compañera.

        }

        // ==========================================
        // CERRAR SESIÓN
        // ==========================================

        tvCerrarSesion.setOnClickListener {

            val intent =
                Intent(
                    requireContext(),
                    MainActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    // ==============================================
    // ACTUALIZAR HOME CADA VEZ QUE VOLVEMOS
    // ==============================================

    override fun onResume() {
        super.onResume()

        actualizarProgresoHome()
    }

    // ==============================================
    // FUNCIÓN DE ACTUALIZACIÓN
    // ==============================================

    private fun actualizarProgresoHome() {

        val viewActual =
            view ?: return

        val tvProgresoContinuar =
            viewActual.findViewById<TextView>(
                R.id.tvProgresoContinuar
            )

        val tvSiguienteLeccion =
            viewActual.findViewById<TextView>(
                R.id.tvSiguienteLeccion
            )

        val progresoContinuar =
            viewActual.findViewById<ProgressBar>(
                R.id.progresoContinuar
            )

        val btnContinuarCurso =
            viewActual.findViewById<Button>(
                R.id.btnContinuarCurso
            )

        val cursoNombre =
            "APIs con Spring Boot"

        val lecciones = listOf(
            Leccion(
                1,
                "Introducción a las APIs",
                "Conoce qué es una API y para qué sirve."
            ),
            Leccion(
                2,
                "Métodos HTTP",
                "Aprende GET, POST, PUT y DELETE."
            ),
            Leccion(
                3,
                "JSON",
                "Aprende cómo se estructura y utiliza JSON."
            ),
            Leccion(
                4,
                "Primer endpoint",
                "Crea tu primer endpoint utilizando Spring Boot."
            )
        )

        val preferencias =
            requireContext().getSharedPreferences(
                "progreso_techmentor",
                Context.MODE_PRIVATE
            )

        var leccionesCompletadas = 0

        lecciones.forEach { leccion ->

            val clave =
                "${cursoNombre}_basico_leccion_${leccion.id}"

            if (
                preferencias.getBoolean(
                    clave,
                    false
                )
            ) {
                leccionesCompletadas++
            }
        }

        val porcentaje =
            (leccionesCompletadas * 100) /
                    lecciones.size

        progresoContinuar.progress =
            porcentaje

        tvProgresoContinuar.text =
            "$porcentaje% completado"

        val siguiente =
            lecciones.firstOrNull { leccion ->

                val clave =
                    "${cursoNombre}_basico_leccion_${leccion.id}"

                !preferencias.getBoolean(
                    clave,
                    false
                )
            }

        if (siguiente != null) {

            tvSiguienteLeccion.text =
                "Siguiente: ${siguiente.nombre}"

            btnContinuarCurso.text =
                "CONTINUAR"

        } else {

            tvSiguienteLeccion.text =
                "🎉 ¡Nivel Básico completado!"

            btnContinuarCurso.text =
                "REVISAR LECCIONES"
        }
    }
}