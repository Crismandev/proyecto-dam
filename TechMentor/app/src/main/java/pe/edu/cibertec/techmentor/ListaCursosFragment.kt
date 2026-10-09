package pe.edu.cibertec.techmentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaCursosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_lista_cursos,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerCursos =
            view.findViewById<RecyclerView>(R.id.recyclerCursos)

        val cursos = listOf(

            Curso(
                1,
                "APIs con Spring Boot",
                "Aprende a crear APIs REST utilizando Spring Boot.",
                "https://placehold.co/300x300/png?text=Spring+Boot"
            ),

            Curso(
                2,
                "Desarrollo web con React",
                "Desarrolla interfaces web modernas utilizando React.",
                "https://placehold.co/300x300/png?text=React"
            ),

            Curso(
                3,
                "Bases de datos con MySQL",
                "Aprende a diseñar y consultar bases de datos.",
                "https://placehold.co/300x300/png?text=MySQL"
            ),

            Curso(
                4,
                "Programación Orientada a Objetos",
                "Comprende clases, objetos, herencia y polimorfismo.",
                "https://placehold.co/300x300/png?text=POO"
            ),

            Curso(
                5,
                "Desarrollo móvil con Android",
                "Crea aplicaciones móviles utilizando Android Studio.",
                "https://placehold.co/300x300/png?text=Android"
            )
        )

        recyclerCursos.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerCursos.adapter =
            CursoAdapter(cursos) { curso ->

                val fragment = CursoDetalleFragment()

                val bundle = Bundle()

                bundle.putInt("cursoId", curso.id)
                bundle.putString("cursoNombre", curso.nombre)
                bundle.putString("cursoDescripcion", curso.descripcion)
                bundle.putString("cursoImagen", curso.imagenUrl)

                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.contenedorHome,
                        fragment
                    )
                    .addToBackStack(null)
                    .commit()
            }
    }
}