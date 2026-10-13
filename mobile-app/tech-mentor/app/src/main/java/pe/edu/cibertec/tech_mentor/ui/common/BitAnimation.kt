package pe.edu.cibertec.tech_mentor.ui.common

import android.animation.ValueAnimator
import android.graphics.drawable.Animatable
import android.os.Build
import android.provider.Settings
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import pe.edu.cibertec.tech_mentor.R

/** Mantiene la animación de Bit activa solamente mientras se ve la pantalla. */
fun AppCompatActivity.showAnimatedBit(imageView: ImageView) {
    imageView.setImageResource(R.drawable.ic_bit)
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            val animationsEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ValueAnimator.areAnimatorsEnabled()
            } else {
                Settings.Global.getFloat(
                    contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f
                ) > 0f
            }
            imageView.setImageResource(
                if (animationsEnabled) R.drawable.avd_bit else R.drawable.ic_bit
            )
            (imageView.drawable as? Animatable)?.start()
        }

        override fun onStop(owner: LifecycleOwner) {
            (imageView.drawable as? Animatable)?.stop()
        }
    })
}
