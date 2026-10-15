package pe.edu.cibertec.tech_mentor.ui.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import pe.edu.cibertec.tech_mentor.databinding.FragmentProfileBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCompletedCourseBinding

data class CompletedItem(
    val title: String,
    val strokeColorHex: String,
    val bgColorHex: String
)

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val completedCourses = listOf(
        CompletedItem("Curso de NPM: Gestión de Paquetes y Dependencias en JavaScript", "#F59E0B", "#FFFBEB"),
        CompletedItem("Curso de ECMAScript: Historia y Versiones de JavaScript", "#F59E0B", "#FFFBEB"),
        CompletedItem("Curso de JavaScript: Manipulación del DOM", "#6F42C1", "#F0E6FF"),
        CompletedItem("Curso de Materialize y Diseño Web Moderno", "#10B981", "#ECFDF5")
    )

    private val completedRoutes = listOf(
        CompletedItem("Ruta de Desarrollo Frontend con JavaScript", "#6F42C1", "#F0E6FF"),
        CompletedItem("Ruta de Fundamentos de Programación y Lógica", "#0088CC", "#E6F7FF")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserProfileData()
        setupCertificatesSection()
    }

    private fun setupUserProfileData() {
        val nombres = "Yabet Mathias"
        val apellidos = "Porras Vilca"
        val email = "mathias.porras@cibertec.edu.pe"
        val biografia = "Apasionado por el desarrollo web y móvil. Aprendiendo paso a paso junto a Bit para convertirme en Full Stack Developer."
        val telefono = "987 654 321"
        val ciudad = "Lima, Perú"
        val profesion = "Desarrollador Web"
        val xpTotal = 1450
        val monedas = 320
        val nivelUsuario = 5
        val rachaActual = 7
        val rachaMaxima = 21

        binding.tvFullName.text = "$nombres $apellidos"
        binding.tvEmail.text = email
        binding.tvBio.text = biografia
        binding.tvPhone.text = telefono
        binding.tvCity.text = ciudad
        binding.tvProfession.text = profesion
        binding.tvUserLevel.text = "Nvl. $nivelUsuario"

        Glide.with(this)
            .load("https://avatars.githubusercontent.com/u/145881432?v=4")
            .into(binding.ivAvatar)

        binding.tvCurrentStreak.text = "$rachaActual días"
        binding.tvMaxStreak.text = "$rachaMaxima días"
        binding.tvTotalXp.text = "$xpTotal XP"
        binding.tvCoins.text = monedas.toString()
    }

    private fun setupCertificatesSection() {
        val adapter = CompletedAdapter(completedCourses)
        binding.rvCompletedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompletedItems.adapter = adapter

        binding.tabLayoutCompleted.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> adapter.updateList(completedCourses)
                    1 -> adapter.updateList(completedRoutes)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CompletedAdapter(private var items: List<CompletedItem>) :
    RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    inner class CompletedViewHolder(val binding: ItemCompletedCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompletedItem) {
            binding.tvCompletedTitle.text = item.title

            val strokeColor = Color.parseColor(item.strokeColorHex)
            val bgColor = Color.parseColor(item.bgColorHex)

            binding.cvCourseIcon.strokeColor = strokeColor
            binding.cvCourseIcon.setCardBackgroundColor(bgColor)
            binding.ivCourseIcon.imageTintList = ColorStateList.valueOf(strokeColor)
        }
    }

    fun updateList(newItems: List<CompletedItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val binding = ItemCompletedCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CompletedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}