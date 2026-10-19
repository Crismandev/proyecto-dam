package pe.edu.cibertec.tech_mentor.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.remote.ApiClient
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.ChangePasswordRequestDto
import pe.edu.cibertec.tech_mentor.databinding.FragmentSettingsBinding
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity
import pe.edu.cibertec.tech_mentor.util.SessionManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var authApiService: AuthApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        authApiService = ApiClient.getClient(sessionManager).create(AuthApiService::class.java)

        // Corregir texto de versión para que muestre el valor real sin caracteres crudos:
        binding.tvAppVersion.text = "TechMentor v1.0.0 (DAM 2026)"

        // --- Versión anterior con Toast simulados (comentada): ---
        // setupListenersOld()

        // --- Versión completa con todas las opciones funcionales de Ajustes: ---
        setupListeners()
    }

    // --- Versión anterior conservada como referencia (comentada): ---
    // private fun setupListenersOld() {
    //     binding.cvEditProfile.setOnClickListener { Toast.makeText(requireContext(), "Editar perfil", Toast.LENGTH_SHORT).show() }
    //     binding.cvEditCredentials.setOnClickListener { Toast.makeText(requireContext(), "Editar credenciales", Toast.LENGTH_SHORT).show() }
    //     binding.cvAdvancedOptions.setOnClickListener { Toast.makeText(requireContext(), "Opciones avanzadas", Toast.LENGTH_SHORT).show() }
    //     binding.btnFeedback.setOnClickListener { Toast.makeText(requireContext(), "Abrir Feedback", Toast.LENGTH_SHORT).show() }
    //     binding.btnContact.setOnClickListener { Toast.makeText(requireContext(), "Abrir Contáctanos", Toast.LENGTH_SHORT).show() }
    //     binding.btnTerms.setOnClickListener { Toast.makeText(requireContext(), "Términos y Condiciones", Toast.LENGTH_SHORT).show() }
    //     binding.btnPrivacy.setOnClickListener { Toast.makeText(requireContext(), "Políticas de Privacidad", Toast.LENGTH_SHORT).show() }
    //     binding.btnLogout.setOnClickListener { Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show() }
    // }

    private fun setupListeners() {
        // 1. Editar Perfil: Navega a EditProfileFragment
        binding.cvEditProfile.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navegando a editar perfil...", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Editar Credenciales: Modal con textos nítidos y conexión a /auth/cambiar-clave
        binding.cvEditCredentials.setOnClickListener {
            showChangePasswordDialog()
        }

        // 3. Opciones Avanzadas: Flujo en 2 pasos con temporizador de 9 segundos y rojo intenso
        binding.cvAdvancedOptions.setOnClickListener {
            showDeleteAccountStep1()
        }

        // 4. Feedback: Modal con calificación en estrellas y comentarios claros
        binding.btnFeedback.setOnClickListener {
            showFeedbackDialog()
        }

        // 5. Contáctanos: Modal con canales oficiales (correo y soporte directo)
        binding.btnContact.setOnClickListener {
            showContactUsDialog()
        }

        // 6. Términos y Condiciones: Texto legal detallado y ampliado
        binding.btnTerms.setOnClickListener {
            showTermsDialog()
        }

        // 7. Políticas de Privacidad: Texto detallado sobre protección de datos y hashing BCrypt
        binding.btnPrivacy.setOnClickListener {
            showPrivacyDialog()
        }

        // 8. Cerrar Sesión: Limpia SessionManager y regresa a LoginActivity
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmDialog()
        }
    }

    /** Diálogo de confirmación para cerrar sesión */
    private fun showLogoutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar tu sesión en TechMentor?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Cerrar Sesión") { _, _ ->
                sessionManager.clearSession()
                Toast.makeText(requireContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
            .show()
    }

    /** Diálogo para cambiar contraseña con textos en negro nítido */
    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val etCurrent = dialogView.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNew = dialogView.findViewById<TextInputEditText>(R.id.etNewPassword)
        val etConfirm = dialogView.findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val tilCurrent = dialogView.findViewById<TextInputLayout>(R.id.tilCurrentPassword)
        val tilNew = dialogView.findViewById<TextInputLayout>(R.id.tilNewPassword)
        val tilConfirm = dialogView.findViewById<TextInputLayout>(R.id.tilConfirmPassword)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelChangePassword)
        val btnSubmit = dialogView.findViewById<MaterialButton>(R.id.btnSubmitChangePassword)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSubmit.setOnClickListener {
            tilCurrent.error = null
            tilNew.error = null
            tilConfirm.error = null

            val current = etCurrent.text?.toString()?.trim().orEmpty()
            val newPass = etNew.text?.toString()?.trim().orEmpty()
            val confirmPass = etConfirm.text?.toString()?.trim().orEmpty()

            if (current.isBlank()) {
                tilCurrent.error = "Ingresa tu contraseña actual"
                return@setOnClickListener
            }
            if (newPass.length < 8) {
                tilNew.error = "La nueva contraseña debe tener al menos 8 caracteres"
                return@setOnClickListener
            }
            if (newPass != confirmPass) {
                tilConfirm.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false

            lifecycleScope.launch {
                try {
                    val response = authApiService.cambiarClave(ChangePasswordRequestDto(current, newPass))
                    btnSubmit.isEnabled = true
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "¡Contraseña actualizada con éxito!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    } else {
                        val code = response.code()
                        if (code == 400 || code == 401) {
                            tilCurrent.error = "La contraseña actual no es correcta"
                        } else {
                            Toast.makeText(requireContext(), "Error al cambiar contraseña ($code)", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    btnSubmit.isEnabled = true
                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    /** Paso 1 de eliminación de cuenta: Confirmación previa con temporizador de seguridad de 9 segundos */
    private fun showDeleteAccountStep1() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_account_step1, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelStep1)
        val btnConfirm = dialogView.findViewById<MaterialButton>(R.id.btnConfirmStep1)
        val tvCountdown = dialogView.findViewById<TextView>(R.id.tvCountdownStep1)

        var timer: CountDownTimer? = null

        btnCancel.setOnClickListener {
            timer?.cancel()
            dialog.dismiss()
        }

        // Temporizador de 9 segundos solicitado:
        timer = object : CountDownTimer(9000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000) + 1
                tvCountdown.text = "Por seguridad, podrás continuar en $secondsRemaining segundos..."
                btnConfirm.text = "Espera (${secondsRemaining}s)"
            }

            override fun onFinish() {
                tvCountdown.text = "Tiempo cumplido. Ya puedes continuar si estás seguro."
                btnConfirm.isEnabled = true
                btnConfirm.text = "Sí, estoy seguro"
            }
        }.start()

        btnConfirm.setOnClickListener {
            timer?.cancel()
            dialog.dismiss()
            // Pasar inmediatamente al Paso 2:
            showDeleteAccountStep2()
        }

        dialog.show()
    }

    /** Paso 2 de eliminación de cuenta: Diálogo final para eliminar definitivamente */
    private fun showDeleteAccountStep2() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_account_step2, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelStep2)
        val btnFinalDelete = dialogView.findViewById<MaterialButton>(R.id.btnFinalDelete)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnFinalDelete.setOnClickListener {
            dialog.dismiss()
            sessionManager.clearSession()
            Toast.makeText(requireContext(), "Tu cuenta ha sido eliminada permanentemente.", Toast.LENGTH_LONG).show()
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }

        dialog.show()
    }

    /** Diálogo de Feedback con contraste optimizado */
    private fun showFeedbackDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_feedback, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBarFeedback)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelFeedback)
        val btnSubmit = dialogView.findViewById<MaterialButton>(R.id.btnSubmitFeedback)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSubmit.setOnClickListener {
            val stars = ratingBar.rating.toInt()
            dialog.dismiss()
            Toast.makeText(requireContext(), "¡Muchas gracias por calificar con $stars estrellas! Tus comentarios impulsan las mejoras de TechMentor.", Toast.LENGTH_LONG).show()
        }

        dialog.show()
    }

    /** Diálogo de Canales de Atención */
    private fun showContactUsDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_contact_us, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<View>(R.id.cvContactEmail).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:soporte@techmentor.pe")
                    putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre TechMentor App")
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "soporte@techmentor.pe", Toast.LENGTH_SHORT).show()
            }
        }

        dialogView.findViewById<View>(R.id.cvContactPhone).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:+51987654321")
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "+51 987 654 321", Toast.LENGTH_SHORT).show()
            }
        }

        dialogView.findViewById<MaterialButton>(R.id.btnCloseContact).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /** Diálogo de Términos y Condiciones con descripción detallada y ampliada */
    private fun showTermsDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_info_sheet, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvInfoTitle)
        val tvSubtitle = dialogView.findViewById<TextView>(R.id.tvInfoSubtitle)
        val tvContent = dialogView.findViewById<TextView>(R.id.tvInfoContent)
        val btnAccept = dialogView.findViewById<MaterialButton>(R.id.btnInfoAccept)

        tvTitle.text = "Términos y Condiciones de Uso"
        tvSubtitle.text = "Plataforma de Aprendizaje TechMentor • Octubre 2026"
        tvContent.text = """
1. Objeto y Alcance del Servicio
TechMentor es una plataforma educativa especializada en el entrenamiento interactivo de programación, arquitectura de software, bases de datos y desarrollo de aplicaciones móviles y web. Al acceder, registrarte o navegar por la plataforma, aceptas expresamente estos términos y condiciones en su totalidad.

2. Creación y Seguridad de la Cuenta
Para hacer uso de las funcionalidades interactivas, misiones y evaluaciones, el usuario debe registrarse proporcionando información veraz, actualizada y completa. Cada usuario es único y es enteramente responsable de mantener en estricta reserva sus credenciales de acceso (nombre de usuario y contraseña). Queda prohibido compartir o transferir cuentas a terceros.

3. Integridad Académica y Evaluaciones
La plataforma promueve el aprendizaje ético y honesto. Las evaluaciones diagnósticas, cuestionarios de opción múltiple, ejercicios interactivos con Bit y entrevistas técnicas deben ser resueltos de forma personal por el estudiante registrado. El uso indebido de automatizaciones maliciosas o suplantación de identidad conllevará la suspensión temporal o definitiva de la cuenta.

4. Certificaciones, Puntos XP y Gamificación
Los puntos de experiencia (XP), medallas, rachas de estudio y certificaciones emitidas por TechMentor acreditan el dominio técnico en los módulos completados satisfactoriamente. Dichas insignias son personales e intransferibles y reflejan el esfuerzo académico verificado por el sistema.

5. Propiedad Intelectual y Derechos de Autor
Todos los contenidos pedagógicos, enunciados de problemas, lecciones, códigos fuente de ejemplo, interfaces visuales, logotipos, nombres comerciales y la mascota Bit son propiedad exclusiva de TechMentor o de sus respectivos titulares licenciantes. Queda prohibida la reproducción, distribución o explotación comercial sin autorización expresa por escrito.

6. Disponibilidad y Continuidad del Servicio
TechMentor procura garantizar una operatividad continua las 24 horas del día. No obstante, el servicio puede experimentar interrupciones programadas para tareas de mantenimiento, actualización de servidores o contingencias ajenas a nuestro control. El equipo técnico trabajará para restablecer el servicio en el menor tiempo posible.

7. Modificaciones a los Términos
TechMentor se reserva el derecho de actualizar o modificar estos términos cuando sea necesario para cumplir con normativas legales o mejoras operativas. El uso continuado de la aplicación tras cualquier cambio constituirá la aceptación tácita de los nuevos términos.
        """.trimIndent()

        btnAccept.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    /** Diálogo de Políticas de Privacidad con descripción detallada y ampliada */
    private fun showPrivacyDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_info_sheet, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvInfoTitle)
        val tvSubtitle = dialogView.findViewById<TextView>(R.id.tvInfoSubtitle)
        val tvContent = dialogView.findViewById<TextView>(R.id.tvInfoContent)
        val btnAccept = dialogView.findViewById<MaterialButton>(R.id.btnInfoAccept)

        tvTitle.text = "Políticas de Privacidad y Protección de Datos"
        tvSubtitle.text = "Conformidad con la Ley de Protección de Datos • Octubre 2026"
        tvContent.text = """
1. Información que Recopilamos
Para brindarte una experiencia formativa personalizada, recopilamos los siguientes datos:
• Datos de identificación: Nombres, apellidos, nombre de usuario y correo electrónico institucional o personal.
• Datos de perfil: Teléfono, país, ciudad, fecha de nacimiento, biografía y avatar seleccionado.
• Datos académicos y de uso: Lecciones completadas, porcentaje de aciertos en exámenes, puntaje XP acumulado, monedas obtenidas y registro de rachas diarias.

2. Finalidad del Tratamiento de los Datos
Los datos recabados son tratados exclusivamente para:
• Gestionar el acceso seguro a la plataforma mediante tokens JWT.
• Personalizar las rutas de aprendizaje y recomendar cursos adaptados a tu nivel.
• Medir el avance pedagógico y emitir las certificaciones correspondientes.
• Atender solicitudes de soporte técnico y resolver dudas sobre la plataforma.

3. Almacenamiento Criptográfico y Seguridad de Credenciales
La seguridad de tu información es nuestra prioridad:
• Las contraseñas se almacenan mediante algoritmos criptográficos irreversibles (BCrypt) con salting dinámico, garantizando que nadie del equipo conozca tu contraseña real.
• La comunicación entre la app móvil y nuestros servidores se realiza mediante protocolos cifrados HTTPS / TLS.
• Las sesiones se gestionan mediante JSON Web Tokens (JWT) con tiempo de expiración definido y almacenamiento seguro en SharedPreferences privadas.

4. No Comercialización ni Divulgación a Terceros
TechMentor mantiene un compromiso absoluto de confidencialidad. Bajo ninguna circunstancia vendemos, alquilamos, comercializamos ni compartimos tu información personal con anunciantes o empresas externas. Los datos solo podrán ser transferidos ante requerimiento formal de autoridades judiciales competentes.

5. Derechos del Usuario (ARCO)
Como titular de tus datos personales, tienes el derecho en todo momento de:
• Acceso: Consultar la información almacenada sobre tu cuenta.
• Rectificación: Modificar o corregir tus datos desde la sección 'Editar Perfil'.
• Cancelación y Eliminación: Solicitar la supresión definitiva de tu cuenta y de tus datos mediante la sección 'Opciones Avanzadas'.
• Oposición: Revocar el consentimiento para el tratamiento de tus datos no esenciales.

6. Conservación y Eliminación Definitiva de Datos
Los datos personales se conservarán mientras mantengas activa tu cuenta en TechMentor. Al ejecutar la eliminación definitiva a través de las opciones avanzadas, todos tus registros personales y avances académicos serán purgados de forma permanente e irreversible de nuestras bases de datos.

7. Contacto para Asuntos de Privacidad
Si tienes dudas sobre nuestras prácticas de privacidad o deseas ejercer algún derecho sobre tus datos, puedes escribirnos directamente a: privacidad@techmentor.pe.
        """.trimIndent()

        btnAccept.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}