package pe.edu.cibertec.techmentor

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_login,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnIniciarSesion =
            view.findViewById<Button>(R.id.btnIniciarSesion)

        val btnGoogle =
            view.findViewById<Button>(R.id.btnGoogle)

        val tvCrearCuenta =
            view.findViewById<TextView>(R.id.tvCrearCuenta)

        val tvRecuperarPassword =
            view.findViewById<TextView>(R.id.tvRecuperarPassword)

        // Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Configuración de Google
        val googleOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), googleOptions)

        // Crear cuenta
        val textoCrearCuenta = "¿Es tu primera vez? Crea tu cuenta"

        val spannable = SpannableString(textoCrearCuenta)

        val inicio = textoCrearCuenta.indexOf("Crea tu cuenta")
        val fin = inicio + "Crea tu cuenta".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.contenedorFragment,
                        CrearCuentaFragment()
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }

        spannable.setSpan(
            clickableSpan,
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvCrearCuenta.text = spannable

        tvCrearCuenta.movementMethod =
            android.text.method.LinkMovementMethod.getInstance()

        // Iniciar sesión normal
        btnIniciarSesion.setOnClickListener {

            val intent = Intent(
                requireContext(),
                HomeActivity::class.java
            )

            startActivity(intent)
        }

        // Recuperar contraseña
        tvRecuperarPassword.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorFragment,
                    RecuperarPasswordFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        // Iniciar sesión con Google
        btnGoogle.setOnClickListener {
            iniciarSesionConGoogle()
        }
    }

    private fun iniciarSesionConGoogle() {

        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(
            signInIntent,
            RC_GOOGLE_SIGN_IN
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                val credential =
                    GoogleAuthProvider.getCredential(
                        account.idToken,
                        null
                    )

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { loginTask ->

                        if (loginTask.isSuccessful) {

                            Toast.makeText(
                                requireContext(),
                                "¡Bienvenido a TechMentor!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(
                                requireContext(),
                                HomeActivity::class.java
                            )

                            startActivity(intent)

                            requireActivity().finish()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "No se pudo iniciar sesión con Google",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            } catch (e: ApiException) {

                Toast.makeText(
                    requireContext(),
                    "No se pudo seleccionar la cuenta de Google",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}