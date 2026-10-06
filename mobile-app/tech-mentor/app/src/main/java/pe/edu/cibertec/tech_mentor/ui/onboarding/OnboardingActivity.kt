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
import pe.edu.cibertec.tech_mentor.ui.register.RegistroActivity

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
                val intent = Intent(this, RegistroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.tvOmitir.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
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