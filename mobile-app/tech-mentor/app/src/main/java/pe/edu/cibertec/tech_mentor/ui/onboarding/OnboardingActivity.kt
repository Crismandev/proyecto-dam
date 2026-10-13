package pe.edu.cibertec.tech_mentor.ui.onboarding

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.databinding.ActivityOnboardingBinding
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private var leavingOnboarding = false

    private val pageCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateSelectedPage(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        setupTextViewColors()

        val restoredPage = savedInstanceState?.getInt(STATE_PAGE) ?: 0
        binding.viewPagerOnboarding.setCurrentItem(
            restoredPage.coerceIn(0, onboardingAdapter.itemCount - 1), false
        )
        updateSelectedPage(binding.viewPagerOnboarding.currentItem)

        binding.btnEmpezarCamino.post {
            binding.btnEmpezarCamino.requestLayout()
        }
    }

    private fun setupViewPager() {
        val onboardingItems = listOf(
            OnboardingItem(
                artIconResId = R.drawable.ic_clock,
                artLabel = getString(R.string.onboarding_art_short_lessons),
                artSecondaryLabel = getString(R.string.onboarding_art_one_step),
                title = "Tu futuro tech\nempieza aquí.",
                description = "Aprende tecnología y prepárate para tu próxima oportunidad, a tu ritmo.",
                cardIconResId = R.drawable.ic_code_purple,
                cardTitle = "De la teoría a lo que puedes crear",
                cardSubtitle = "Explora React, bases de datos, APIs y desarrollo móvil."
            ),
            OnboardingItem(
                artIconResId = R.drawable.ic_onboarding_target,
                artLabel = getString(R.string.onboarding_art_no_pressure),
                artSecondaryLabel = getString(R.string.onboarding_art_your_level),
                title = "Pequenos pasos.\nHabilidades reales.",
                description = "Avanza con lecciones cortas, preguntas y explicaciones que te ayuden a entender.",
                cardIconResId = R.drawable.ic_path_purple,
                cardTitle = "Tu progreso, a tu ritmo",
                cardSubtitle = "Sigue una ruta y celebra cada leccion completada."
            ),
            OnboardingItem(
                artIconResId = R.drawable.ic_book_open,
                artLabel = getString(R.string.onboarding_art_your_pace),
                artSecondaryLabel = getString(R.string.onboarding_art_your_progress),
                title = "Conoce a Bit.\nTu mentor virtual.",
                description = "Practica entrevistas técnicas con un robot paciente que te ayuda a organizar ideas.",
                cardIconResId = R.drawable.ic_chats_purple,
                cardTitle = "Llega con más confianza.",
                cardSubtitle = "Ensaya respuestas, revisa consejos y vuelve a intentarlo."
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
        lifecycle.addObserver(onboardingAdapter)
        binding.viewPagerOnboarding.adapter = onboardingAdapter
        binding.indicadorPuntos.attachTo(binding.viewPagerOnboarding)
    }

    private fun setupListeners() {
        binding.viewPagerOnboarding.registerOnPageChangeCallback(pageCallback)
        binding.btnEmpezarCamino.setOnClickListener {
            val nextItem = binding.viewPagerOnboarding.currentItem + 1
            if (nextItem < onboardingAdapter.itemCount) {
                binding.viewPagerOnboarding.currentItem = nextItem
            } else {
                irIniciarSesion()
            }
        }
        binding.tvOmitir.setOnClickListener { irIniciarSesion() }

        onBackPressedDispatcher.addCallback(this) {
            val currentPage = binding.viewPagerOnboarding.currentItem
            if (currentPage > 0) {
                binding.viewPagerOnboarding.currentItem = currentPage - 1
            } else {
                finish()
            }
        }
    }

    private fun updateSelectedPage(position: Int) {
        binding.btnEmpezarCamino.text = if (position == onboardingAdapter.itemCount - 1) {
            "Empezar mi camino"
        } else {
            getString(R.string.onboarding_continue)
        }
        onboardingAdapter.setCurrentPage(position)
    }

    private fun setupTextViewColors() {
        val textoCompleto = "¿Ya tienes cuenta? Inicia sesión"
        val spannableString = SpannableString(textoCompleto)
        val startIndex = textoCompleto.indexOf("Inicia sesión")
        val endIndex = startIndex + "Inicia sesión".length
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                irIniciarSesion()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(this@OnboardingActivity, R.color.purple)
            }
        }
        spannableString.setSpan(
            clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvYaTienesCuenta.text = spannableString
        binding.tvYaTienesCuenta.movementMethod = LinkMovementMethod.getInstance()
        binding.tvYaTienesCuenta.highlightColor = Color.TRANSPARENT
    }

    private fun irIniciarSesion() {
        if (leavingOnboarding) return
        leavingOnboarding = true
        SessionManager(this).setOnboardingVisto()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_PAGE, binding.viewPagerOnboarding.currentItem)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        binding.viewPagerOnboarding.unregisterOnPageChangeCallback(pageCallback)
        super.onDestroy()
    }

    companion object {
        private const val STATE_PAGE = "onboarding_page"
    }
}

/*
 * Versión anterior conservada por solicitud de la usuaria.
 * La versión activa usa ilustraciones locales y unifica la salida a Login, conservando el paso y la preferencia de bienvenida vista.
 * El código siguiente permanece únicamente como referencia y no se ejecuta.

package pe.edu.cibertec.tech_mentor.ui.onboarding

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.ActivityOnboardingBinding
import pe.edu.cibertec.tech_mentor.ui.login.LoginActivity
import pe.edu.cibertec.tech_mentor.ui.register.RegisterActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        setupTextViewColors()

        binding.btnEmpezarCamino.post {
            binding.btnEmpezarCamino.requestLayout()
        }
    }

    private fun setupViewPager() {
        val onboardingItems = listOf(
            OnboardingItem(
                imageUrl = "https://i.ytimg.com/vi/GoM0W31hlQM/maxresdefault.jpg",
                title = "Tu futuro tech\nempieza aquí.",
                description = "Aprende tecnología y prepárate para tu próxima oportunidad, a tu ritmo.",
                cardIconResId = R.drawable.ic_code_purple,
                cardTitle = "De la teoría a lo que puedes crear",
                cardSubtitle = "Explora React, bases de datos, APIs y desarrollo móvil."
            ),
            OnboardingItem(
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSynSVUvhMQzMyChUyEqHL9kiUwH3036ZY3NK06I7dUKRgZnzf8rXDxk4bW&s=10",
                title = "Pequenos pasos.\nHabilidades reales.",
                description = "Avanza con lecciones cortas, preguntas y explicaciones que te ayuden a entender.",
                cardIconResId = R.drawable.ic_path_purple,
                cardTitle = "Tu progreso, a tu ritmo",
                cardSubtitle = "Sigue una ruta y celebra cada leccion completada."
            ),
            OnboardingItem(
                imageUrl = "https://esportsinsider.com/wp-content/uploads/2025/05/LEVIATAN-esports-valorant-edited.jpg",
                title = "Conoce a Bit.\nTu mentor virtual.",
                description = "Practica entrevistas técnicas con un robot paciente que te ayuda a organizar ideas.",
                cardIconResId = R.drawable.ic_chats_purple,
                cardTitle = "Llega con más confianza.",
                cardSubtitle = "Ensaya respuestas, revisa consejos y vuelve a intentarlo."
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
        binding.viewPagerOnboarding.adapter = onboardingAdapter
        binding.indicadorPuntos.attachTo(binding.viewPagerOnboarding)
    }

    private fun setupListeners() {

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == onboardingAdapter.itemCount - 1) {
                    binding.btnEmpezarCamino.text = "Empezar mi camino"
                } else {
                    binding.btnEmpezarCamino.text = "Continuar"
                }
            }
        })

        binding.btnEmpezarCamino.setOnClickListener {
            val nextItem = binding.viewPagerOnboarding.currentItem + 1

            if (nextItem < onboardingAdapter.itemCount) {
                binding.viewPagerOnboarding.currentItem = nextItem
            } else {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.tvOmitir.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupTextViewColors() {
        val textoCompleto = "¿Ya tienes cuenta? Inicia sesión"
        val spannableString = SpannableString(textoCompleto)

        val startIndex = textoCompleto.indexOf("Inicia sesión")
        val endIndex = startIndex + "Inicia sesión".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#7C4DFF")
            }
        }

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvYaTienesCuenta.text = spannableString
        binding.tvYaTienesCuenta.movementMethod = LinkMovementMethod.getInstance()
        binding.tvYaTienesCuenta.highlightColor = Color.TRANSPARENT
    }

    private fun irIniciarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
*/
