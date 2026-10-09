package pe.edu.cibertec.techmentor

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class CrearCuentaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_crear_cuenta,
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
        // CAMPOS
        // ==========================================

        val etNombres =
            view.findViewById<EditText>(R.id.etNombres)

        val etApellidos =
            view.findViewById<EditText>(R.id.etApellidos)

        val etCorreo =
            view.findViewById<EditText>(R.id.etCorreo)

        val etFechaNacimiento =
            view.findViewById<EditText>(R.id.etFechaNacimiento)

        val etTelefono =
            view.findViewById<EditText>(R.id.etTelefono)

        val etPais =
            view.findViewById<EditText>(R.id.etPais)

        val etCiudad =
            view.findViewById<EditText>(R.id.etCiudad)

        val etBiografia =
            view.findViewById<EditText>(R.id.etBiografia)

        val etProfesion =
            view.findViewById<EditText>(R.id.etProfesion)

        val etPasswordRegistro =
            view.findViewById<EditText>(
                R.id.etPasswordRegistro
            )

        val btnRegistrar =
            view.findViewById<Button>(
                R.id.btnRegistrar
            )

        val tvVolverLogin =
            view.findViewById<TextView>(
                R.id.tvVolverLogin
            )

        // ==========================================
        // CALENDARIO
        // ==========================================

        etFechaNacimiento.setOnClickListener {

            val calendario =
                Calendar.getInstance()

            val año =
                calendario.get(Calendar.YEAR)

            val mes =
                calendario.get(Calendar.MONTH)

            val dia =
                calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker =
                DatePickerDialog(
                    requireContext(),
                    { _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->

                        val fecha =
                            String.format(
                                "%02d/%02d/%04d",
                                diaSeleccionado,
                                mesSeleccionado + 1,
                                añoSeleccionado
                            )

                        etFechaNacimiento.setText(
                            fecha
                        )
                    },
                    año,
                    mes,
                    dia
                )

            datePicker.show()
        }

        // ==========================================
        // REGISTRAR
        // ==========================================

        btnRegistrar.setOnClickListener {

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

            val password =
                etPasswordRegistro.text.toString()

            // ======================================
            // VALIDAR CAMPOS OBLIGATORIOS
            // ======================================

            if (
                nombres.isEmpty() ||
                apellidos.isEmpty() ||
                correo.isEmpty() ||
                fechaNacimiento.isEmpty() ||
                telefono.isEmpty() ||
                pais.isEmpty() ||
                ciudad.isEmpty() ||
                profesion.isEmpty() ||
                password.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Completa todos los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // ======================================
            // VALIDAR CORREO
            // ======================================

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()
            ) {

                etCorreo.error =
                    "Ingresa un correo válido"

                etCorreo.requestFocus()

                return@setOnClickListener
            }

            // ======================================
            // VALIDAR CONTRASEÑA
            // ======================================

            if (password.length < 6) {

                etPasswordRegistro.error =
                    "La contraseña debe tener al menos 6 caracteres"

                etPasswordRegistro.requestFocus()

                return@setOnClickListener
            }

            // ======================================
            // REGISTRO PROVISIONAL
            // ======================================

            val preferencias =
                requireContext().getSharedPreferences(
                    "perfil_techmentor",
                    android.content.Context.MODE_PRIVATE
                )

            preferencias.edit()
                .putString("nombres", nombres)
                .putString("apellidos", apellidos)
                .putString("correo", correo)
                .putString("fechaNacimiento", fechaNacimiento)
                .putString("telefono", telefono)
                .putString("pais", pais)
                .putString("ciudad", ciudad)
                .putString("biografia", biografia)
                .putString("profesion", profesion)
                .putString("password", password)
                .apply()

            Toast.makeText(
                requireContext(),
                "¡Cuenta creada correctamente!",
                Toast.LENGTH_LONG
            ).show()

            parentFragmentManager.popBackStack()

            // Volver al Login

            parentFragmentManager.popBackStack()
        }

        // ==========================================
        // VOLVER AL LOGIN
        // ==========================================

        tvVolverLogin.setOnClickListener {

            parentFragmentManager.popBackStack()
        }
    }
}