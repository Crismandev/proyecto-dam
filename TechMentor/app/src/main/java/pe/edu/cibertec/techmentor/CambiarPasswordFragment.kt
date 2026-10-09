package pe.edu.cibertec.techmentor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class CambiarPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_cambiar_password,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val etPasswordActual =
            view.findViewById<EditText>(
                R.id.etPasswordActual
            )

        val etPasswordNueva =
            view.findViewById<EditText>(
                R.id.etPasswordNueva
            )

        val etConfirmarPassword =
            view.findViewById<EditText>(
                R.id.etConfirmarPassword
            )

        val btnCambiarPassword =
            view.findViewById<Button>(
                R.id.btnCambiarPassword
            )

        val btnCancelar =
            view.findViewById<Button>(
                R.id.btnCancelarPassword
            )

        val preferencias =
            requireContext().getSharedPreferences(
                "perfil_techmentor",
                Context.MODE_PRIVATE
            )

        btnCambiarPassword.setOnClickListener {

            val passwordActual =
                etPasswordActual.text.toString()

            val passwordNueva =
                etPasswordNueva.text.toString()

            val confirmarPassword =
                etConfirmarPassword.text.toString()

            val passwordGuardada =
                preferencias.getString(
                    "password",
                    ""
                ) ?: ""

            // Verificar contraseña actual

            if (passwordActual != passwordGuardada) {

                etPasswordActual.error =
                    "La contraseña actual es incorrecta"

                etPasswordActual.requestFocus()

                return@setOnClickListener
            }

            // Verificar longitud

            if (passwordNueva.length < 6) {

                etPasswordNueva.error =
                    "La contraseña debe tener al menos 6 caracteres"

                etPasswordNueva.requestFocus()

                return@setOnClickListener
            }

            // Verificar coincidencia

            if (passwordNueva != confirmarPassword) {

                etConfirmarPassword.error =
                    "Las contraseñas no coinciden"

                etConfirmarPassword.requestFocus()

                return@setOnClickListener
            }

            // Guardar nueva contraseña

            preferencias.edit()
                .putString(
                    "password",
                    passwordNueva
                )
                .apply()

            Toast.makeText(
                requireContext(),
                "Contraseña actualizada correctamente",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.popBackStack()
        }

        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}