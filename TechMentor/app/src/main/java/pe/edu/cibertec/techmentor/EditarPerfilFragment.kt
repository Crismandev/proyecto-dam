package pe.edu.cibertec.techmentor

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class EditarPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_editar_perfil,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val etNombres =
            view.findViewById<EditText>(R.id.etEditarNombres)

        val etApellidos =
            view.findViewById<EditText>(R.id.etEditarApellidos)

        val etCorreo =
            view.findViewById<EditText>(R.id.etEditarCorreo)

        val etFechaNacimiento =
            view.findViewById<EditText>(
                R.id.etEditarFechaNacimiento
            )

        val etTelefono =
            view.findViewById<EditText>(R.id.etEditarTelefono)

        val etPais =
            view.findViewById<EditText>(R.id.etEditarPais)

        val etCiudad =
            view.findViewById<EditText>(R.id.etEditarCiudad)

        val etBiografia =
            view.findViewById<EditText>(R.id.etEditarBiografia)

        val etProfesion =
            view.findViewById<EditText>(R.id.etEditarProfesion)

        val btnGuardar =
            view.findViewById<Button>(R.id.btnGuardarCambios)

        val btnCancelar =
            view.findViewById<Button>(R.id.btnCancelarEdicion)

        val preferencias =
            requireContext().getSharedPreferences(
                "perfil_techmentor",
                Context.MODE_PRIVATE
            )

        // Cargar datos actuales

        etNombres.setText(
            preferencias.getString("nombres", "")
        )

        etApellidos.setText(
            preferencias.getString("apellidos", "")
        )

        etCorreo.setText(
            preferencias.getString("correo", "")
        )

        etFechaNacimiento.setText(
            preferencias.getString(
                "fechaNacimiento",
                ""
            )
        )

        etTelefono.setText(
            preferencias.getString("telefono", "")
        )

        etPais.setText(
            preferencias.getString("pais", "")
        )

        etCiudad.setText(
            preferencias.getString("ciudad", "")
        )

        etBiografia.setText(
            preferencias.getString("biografia", "")
        )

        etProfesion.setText(
            preferencias.getString("profesion", "")
        )

        // Selector de fecha

        etFechaNacimiento.setOnClickListener {

            val calendario = Calendar.getInstance()

            val año =
                calendario.get(Calendar.YEAR)

            val mes =
                calendario.get(Calendar.MONTH)

            val dia =
                calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, añoSeleccionado,
                  mesSeleccionado,
                  diaSeleccionado ->

                    val fecha = String.format(
                        "%02d/%02d/%04d",
                        diaSeleccionado,
                        mesSeleccionado + 1,
                        añoSeleccionado
                    )

                    etFechaNacimiento.setText(fecha)
                },
                año,
                mes,
                dia
            )

            datePicker.show()
        }

        // Guardar cambios

        btnGuardar.setOnClickListener {

            val nombres =
                etNombres.text.toString().trim()

            val apellidos =
                etApellidos.text.toString().trim()

            val correo =
                etCorreo.text.toString().trim()

            val fechaNacimiento =
                etFechaNacimiento.text.toString().trim()

            val telefono =
                etTelefono.text.toString().trim()

            val pais =
                etPais.text.toString().trim()

            val ciudad =
                etCiudad.text.toString().trim()

            val biografia =
                etBiografia.text.toString().trim()

            val profesion =
                etProfesion.text.toString().trim()

            if (
                nombres.isEmpty() ||
                apellidos.isEmpty() ||
                correo.isEmpty() ||
                fechaNacimiento.isEmpty() ||
                telefono.isEmpty() ||
                pais.isEmpty() ||
                ciudad.isEmpty() ||
                profesion.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Completa todos los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (
                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()
            ) {
                etCorreo.error =
                    "Ingresa un correo válido"

                etCorreo.requestFocus()

                return@setOnClickListener
            }

            preferencias.edit()
                .putString("nombres", nombres)
                .putString("apellidos", apellidos)
                .putString("correo", correo)
                .putString(
                    "fechaNacimiento",
                    fechaNacimiento
                )
                .putString("telefono", telefono)
                .putString("pais", pais)
                .putString("ciudad", ciudad)
                .putString("biografia", biografia)
                .putString("profesion", profesion)
                .apply()

            Toast.makeText(
                requireContext(),
                "Perfil actualizado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.popBackStack()
        }

        // Cancelar

        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}