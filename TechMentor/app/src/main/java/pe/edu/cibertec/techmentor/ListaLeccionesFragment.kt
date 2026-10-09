package pe.edu.cibertec.techmentor

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class ListaLeccionesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_lista_lecciones,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val tvTituloNivel =
            view.findViewById<TextView>(R.id.tvTituloNivel)

        val tvNombreCursoNivel =
            view.findViewById<TextView>(R.id.tvNombreCursoNivel)

        val tvProgresoNivel =
            view.findViewById<TextView>(R.id.tvProgresoNivel)

        val progresoNivel =
            view.findViewById<ProgressBar>(R.id.progresoNivel)

        val contenedorLecciones =
            view.findViewById<LinearLayout>(R.id.contenedorLecciones)

        val cursoNombre =
            arguments?.getString("cursoNombre")
                ?: "Curso"

        val nivel =
            arguments?.getString("nivel")
                ?: "Básico"

        tvTituloNivel.text = "🚀 NIVEL $nivel"
        tvNombreCursoNivel.text = cursoNombre

        // ==========================================
        // LECCIONES DEL NIVEL BÁSICO
        // ==========================================

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

        actualizarProgreso(
            cursoNombre,
            lecciones,
            tvProgresoNivel,
            progresoNivel
        )

        // ==========================================
        // CREAR LAS LECCIONES EN PANTALLA
        // ==========================================

        lecciones.forEach { leccion ->

            val item = TextView(requireContext())

            item.text =
                "${leccion.id}. ${leccion.nombre}\n\n${leccion.descripcion}"

            item.textSize = 16f

            item.setTypeface(
                null,
                Typeface.BOLD
            )

            item.setPadding(
                20,
                20,
                20,
                20
            )

            // ======================================
            // CLICK EN UNA LECCIÓN
            // ======================================

            item.setOnClickListener {

                val fragment =
                    LeccionFragment()

                val bundle =
                    Bundle()

                bundle.putInt(
                    "leccionId",
                    leccion.id
                )

                bundle.putString(
                    "leccionNombre",
                    leccion.nombre
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

            // ======================================
            // MÁRGENES DEL ITEM
            // ======================================

            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(
                0,
                0,
                0,
                12
            )

            item.layoutParams =
                params

            contenedorLecciones.addView(item)
        }
    }

    // ==============================================
    // ACTUALIZAR PROGRESO AL REGRESAR A LA PANTALLA
    // ==============================================

    override fun onResume() {
        super.onResume()

        val cursoNombre =
            arguments?.getString("cursoNombre")
                ?: "Curso"

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

        val tvProgresoNivel =
            view?.findViewById<TextView>(
                R.id.tvProgresoNivel
            )

        val progresoNivel =
            view?.findViewById<ProgressBar>(
                R.id.progresoNivel
            )

        if (tvProgresoNivel != null &&
            progresoNivel != null
        ) {

            actualizarProgreso(
                cursoNombre,
                lecciones,
                tvProgresoNivel,
                progresoNivel
            )
        }
    }

    // ==============================================
    // FUNCIÓN PARA CALCULAR EL PROGRESO
    // ==============================================

    private fun actualizarProgreso(
        cursoNombre: String,
        lecciones: List<Leccion>,
        tvProgresoNivel: TextView,
        progresoNivel: ProgressBar
    ) {

        val preferencias =
            requireContext()
                .getSharedPreferences(
                    "progreso_techmentor",
                    Context.MODE_PRIVATE
                )

        var leccionesCompletadas = 0

        lecciones.forEach { leccion ->

            val claveLeccion =
                "${cursoNombre}_basico_leccion_${leccion.id}"

            val completada =
                preferencias.getBoolean(
                    claveLeccion,
                    false
                )

            if (completada) {
                leccionesCompletadas++
            }
        }

        val progreso =
            if (lecciones.isNotEmpty()) {
                (leccionesCompletadas * 100) /
                        lecciones.size
            } else {
                0
            }

        progresoNivel.progress =
            progreso

        tvProgresoNivel.text =
            "$progreso% completado"
    }
}