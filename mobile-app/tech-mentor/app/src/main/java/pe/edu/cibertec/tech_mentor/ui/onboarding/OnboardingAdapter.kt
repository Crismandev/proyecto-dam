package pe.edu.cibertec.tech_mentor.ui.onboarding

import android.animation.ValueAnimator
import android.graphics.drawable.Animatable
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.ItemOnboardingBinding

class OnboardingAdapter(private val items: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>(), DefaultLifecycleObserver {

    private val attachedHolders = mutableSetOf<OnboardingViewHolder>()
    private var screenStarted = false
    private var selectedPage = 0

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var animating = false

        fun bind(item: OnboardingItem) {
            stopBit()
            binding.ivOnboardingImage.setImageResource(R.drawable.ic_bit)
            binding.ivArtChipIcon.setImageResource(item.artIconResId)
            binding.tvArtChipLabel.text = item.artLabel
            binding.tvArtChipSecondaryLabel.text = item.artSecondaryLabel
            binding.tvOnboardingTitle.text = item.title
            binding.tvOnboardingDescription.text = item.description
            binding.ivCardIcon.setImageResource(item.cardIconResId)
            binding.tvCardTitle.text = item.cardTitle
            binding.tvCardSubtitle.text = item.cardSubtitle
        }

        fun startBit() {
            if (animating) return
            val enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ValueAnimator.areAnimatorsEnabled()
            } else {
                Settings.Global.getFloat(
                    binding.root.context.contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1f
                ) > 0f
            }
            binding.ivOnboardingImage.setImageResource(
                if (enabled) R.drawable.avd_bit else R.drawable.ic_bit
            )
            (binding.ivOnboardingImage.drawable as? Animatable)?.let {
                it.start()
                animating = true
            }
        }

        fun stopBit() {
            (binding.ivOnboardingImage.drawable as? Animatable)?.stop()
            animating = false
        }
    }

    fun setCurrentPage(position: Int) {
        selectedPage = position
        attachedHolders.forEach(::updateAnimation)
    }

    private fun updateAnimation(holder: OnboardingViewHolder) {
        if (screenStarted && holder.bindingAdapterPosition == selectedPage) {
            holder.startBit()
        } else {
            holder.stopBit()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        screenStarted = true
        attachedHolders.forEach(::updateAnimation)
    }

    override fun onStop(owner: LifecycleOwner) {
        screenStarted = false
        attachedHolders.forEach { it.stopBit() }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        attachedHolders.forEach { it.stopBit() }
        attachedHolders.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
        updateAnimation(holder)
    }

    override fun onViewAttachedToWindow(holder: OnboardingViewHolder) {
        super.onViewAttachedToWindow(holder)
        attachedHolders.add(holder)
        updateAnimation(holder)
    }

    override fun onViewDetachedFromWindow(holder: OnboardingViewHolder) {
        holder.stopBit()
        attachedHolders.remove(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: OnboardingViewHolder) {
        holder.stopBit()
        attachedHolders.remove(holder)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = items.size
}

/*
 * Versión anterior conservada por solicitud de la usuaria.
 * La versión activa reemplaza las fotografías remotas por Bit y sus rótulos, y controla la animación según la página y el ciclo de vida.
 * El código siguiente permanece únicamente como referencia y no se ejecuta.

package pe.edu.cibertec.tech_mentor.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.tech_mentor.databinding.ItemOnboardingBinding

class OnboardingAdapter(private val items: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItem) {
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.ivOnboardingImage)

            binding.tvOnboardingTitle.text = item.title
            binding.tvOnboardingDescription.text = item.description

            binding.ivCardIcon.setImageResource(item.cardIconResId)
            binding.tvCardTitle.text = item.cardTitle
            binding.tvCardSubtitle.text = item.cardSubtitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
*/
