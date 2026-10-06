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