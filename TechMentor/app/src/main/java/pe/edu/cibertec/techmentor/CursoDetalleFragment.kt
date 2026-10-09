package pe.edu.cibertec.techmentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class CursoDetalleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_curso_detalle,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val ivCursoDetalle =
            view.findViewById<ImageView>(R.id.ivCursoDetalle)

        val tvCursoDetalleNombre =
            view.findViewById<TextView>(R.id.tvCursoDetalleNombre)

        val tvCursoDetalleDescripcion =
            view.findViewById<TextView>(R.id.tvCursoDetalleDescripcion)

        val btnNivelBasico =
            view.findViewById<Button>(R.id.btnNivelBasico)

        val btnNivelIntermedio =
            view.findViewById<Button>(R.id.btnNivelIntermedio)

        val btnNivelAvanzado =
            view.findViewById<Button>(R.id.btnNivelAvanzado)

        // Recuperamos la información del curso

        val cursoId =
            arguments?.getInt("cursoId", 0) ?: 0

        val cursoNombre =
            arguments?.getString("cursoNombre")
                ?: "Curso"

        val cursoDescripcion =
            arguments?.getString("cursoDescripcion")
                ?: "Sin descripción disponible."

        val cursoImagen =
            arguments?.getString("cursoImagen")
                ?: ""

        // Mostramos la información

        tvCursoDetalleNombre.text = cursoNombre

        tvCursoDetalleDescripcion.text = cursoDescripcion

        Glide.with(this)
            .load(cursoImagen)
            .centerCrop()
            .into(ivCursoDetalle)

        // NIVEL BÁSICO

        btnNivelBasico.setOnClickListener {

            val fragment = ListaLeccionesFragment()

            val bundle = Bundle()
            bundle.putInt("cursoId", cursoId)
            bundle.putString("cursoNombre", cursoNombre)
            bundle.putString("nivel", "Básico")

            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        }

        // NIVEL INTERMEDIO

        btnNivelIntermedio.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Seleccionaste nivel INTERMEDIO de $cursoNombre",
                Toast.LENGTH_SHORT
            ).show()
        }

        // NIVEL AVANZADO

        btnNivelAvanzado.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Seleccionaste nivel AVANZADO de $cursoNombre",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}