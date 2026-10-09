package pe.edu.cibertec.techmentor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_perfil,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombrePerfil =
            view.findViewById<TextView>(R.id.tvNombrePerfil)

        val tvProfesionPerfil =
            view.findViewById<TextView>(R.id.tvProfesionPerfil)

        val tvCorreoPerfil =
            view.findViewById<TextView>(R.id.tvCorreoPerfil)

        val tvFechaNacimientoPerfil =
            view.findViewById<TextView>(
                R.id.tvFechaNacimientoPerfil
            )

        val tvTelefonoPerfil =
            view.findViewById<TextView>(R.id.tvTelefonoPerfil)

        val tvUbicacionPerfil =
            view.findViewById<TextView>(R.id.tvUbicacionPerfil)

        val tvBiografiaPerfil =
            view.findViewById<TextView>(R.id.tvBiografiaPerfil)

        val btnEditarPerfil =
            view.findViewById<Button>(R.id.btnEditarPerfil)

        val btnCambiarPassword =
            view.findViewById<Button>(R.id.btnCambiarPassword)

        cargarDatosPerfil(
            tvNombrePerfil,
            tvProfesionPerfil,
            tvCorreoPerfil,
            tvFechaNacimientoPerfil,
            tvTelefonoPerfil,
            tvUbicacionPerfil,
            tvBiografiaPerfil
        )

        btnEditarPerfil.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    EditarPerfilFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        btnCambiarPassword.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    CambiarPasswordFragment()
                )
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        view?.let { vista ->

            cargarDatosPerfil(
                vista.findViewById(R.id.tvNombrePerfil),
                vista.findViewById(R.id.tvProfesionPerfil),
                vista.findViewById(R.id.tvCorreoPerfil),
                vista.findViewById(
                    R.id.tvFechaNacimientoPerfil
                ),
                vista.findViewById(R.id.tvTelefonoPerfil),
                vista.findViewById(R.id.tvUbicacionPerfil),
                vista.findViewById(R.id.tvBiografiaPerfil)
            )
        }
    }

    private fun cargarDatosPerfil(
        tvNombre: TextView,
        tvProfesion: TextView,
        tvCorreo: TextView,
        tvFechaNacimiento: TextView,
        tvTelefono: TextView,
        tvUbicacion: TextView,
        tvBiografia: TextView
    ) {

        val preferencias =
            requireContext().getSharedPreferences(
                "perfil_techmentor",
                Context.MODE_PRIVATE
            )

        val nombres =
            preferencias.getString(
                "nombres",
                "Luis"
            ) ?: "Luis"

        val apellidos =
            preferencias.getString(
                "apellidos",
                "Pinedo"
            ) ?: "Pinedo"

        val profesion =
            preferencias.getString(
                "profesion",
                "Estudiante de Computación e Informática"
            ) ?: ""

        val correo =
            preferencias.getString(
                "correo",
                "prueba@gmail.com"
            ) ?: ""

        val fechaNacimiento =
            preferencias.getString(
                "fechaNacimiento",
                "No registrada"
            ) ?: ""

        val telefono =
            preferencias.getString(
                "telefono",
                "No registrado"
            ) ?: ""

        val pais =
            preferencias.getString(
                "pais",
                "Perú"
            ) ?: ""

        val ciudad =
            preferencias.getString(
                "ciudad",
                "Lima"
            ) ?: ""

        val biografia =
            preferencias.getString(
                "biografia",
                "Sin biografía"
            ) ?: ""

        tvNombre.text =
            "$nombres $apellidos"

        tvProfesion.text =
            profesion

        tvCorreo.text =
            "📧 $correo"

        tvFechaNacimiento.text =
            "🎂 $fechaNacimiento"

        tvTelefono.text =
            "📱 $telefono"

        tvUbicacion.text =
            "🌎 $pais - $ciudad"

        tvBiografia.text =
            "📝 $biografia"
    }
}