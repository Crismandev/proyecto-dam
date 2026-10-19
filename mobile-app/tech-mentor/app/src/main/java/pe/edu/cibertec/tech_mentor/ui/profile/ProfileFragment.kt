package pe.edu.cibertec.tech_mentor.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import pe.edu.cibertec.tech_mentor.R
import pe.edu.cibertec.tech_mentor.databinding.FragmentProfileBinding
import pe.edu.cibertec.tech_mentor.databinding.ItemCompletedCourseBinding
import pe.edu.cibertec.tech_mentor.util.AvatarUrls
import pe.edu.cibertec.tech_mentor.util.SessionManager
import kotlin.random.Random

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Vive en MainActivity: sobrevive al cambio de pestaña
    private val vm: ProfileViewModel by activityViewModels {
        ProfileViewModel.Factory(requireContext().applicationContext)
    }

    private lateinit var sessionManager: SessionManager
    private lateinit var completedAdapter: CompletedAdapter
    private var state = ProfileUiState()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        setupListeners()
        setupCompletedList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { render(it) }
            }
        }
        vm.refresh()
    }

    private fun setupListeners() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
        binding.btnEditBio.setOnClickListener {
            showEditBiographyDialog()
        }
        binding.cvAvatar.setOnClickListener {
            shuffleAvatar()
        }
    }

    private fun setupCompletedList() {
        completedAdapter = CompletedAdapter(emptyList())
        binding.rvCompletedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompletedItems.adapter = completedAdapter

        binding.tabLayoutCompleted.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = showCompleted()
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showCompleted() {
        completedAdapter.updateList(
            if (binding.tabLayoutCompleted.selectedTabPosition == 1) state.completedRoutes
            else state.completedCourses
        )
    }

    private fun render(s: ProfileUiState) {
        state = s

        val username = sessionManager.getUsername() ?: "Estudiante"
        val role = sessionManager.getUserRole() ?: "ALUMNO"
        val user = s.user

        if (user == null) {
            // Valores por defecto mientras llega el perfil
            binding.tvFullName.text = username
            binding.tvEmail.text = if (username.contains("@")) username else "$username@techmentor.com"
            binding.tvUserLevel.text = "Nvl. 1"
            binding.tvCurrentStreak.text = "0 días"
            binding.tvMaxStreak.text = "0 días"
            binding.tvTotalXp.text = "0 XP"
            binding.tvCoins.text = "0"
        } else {
            binding.tvFullName.text = "${user.nombres} ${user.apellidos}".trim()
            binding.tvEmail.text = user.email
            binding.tvBio.text = user.biografia?.takeIf { it.isNotBlank() } ?: "Aún no has escrito una biografía."

            val location = listOfNotNull(user.ciudad, user.pais).joinToString(", ")
            binding.tvCity.text = location.ifBlank { "Ubicación desconocida" }
            binding.tvPhone.text = user.telefono.orEmpty()

            binding.tvUserLevel.text = "Nvl. ${user.nivelUsuario}"
            binding.tvCurrentStreak.text = "${user.rachaActual} días"
            binding.tvMaxStreak.text = "${user.rachaMaxima} días"
            binding.tvTotalXp.text = "${user.xpTotal} XP"
            binding.tvCoins.text = user.monedas.toString()
            loadAvatar(user.avatarUrl, user.nombres.ifBlank { username })
        }

        binding.tvProfession.text = s.profession ?: role
        if (user == null) {
            loadAvatar(null, username)
        }
        showCompleted()
    }

    private fun loadAvatar(avatarUrl: String?, seed: String) {
        val url = AvatarUrls.of(seed, avatarUrl)
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(binding.ivAvatar)
    }

    private fun shuffleAvatar() {
        val seed = (sessionManager.getUsername() ?: "techmentor") + "-" + Random.nextInt(100_000)
        val url = AvatarUrls.random(seed)
        Glide.with(this).load(url).into(binding.ivAvatar)
        vm.updateAvatar(url)
    }

    private fun showEditBiographyDialog() {
        val current = state.user?.biografia.orEmpty()
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.hint_biography)
            setText(current)
            setPadding(48, 32, 48, 32)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.action_edit_biography)
            .setView(input)
            .setNegativeButton(R.string.btn_cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                val text = input.text?.toString()?.trim().orEmpty()
                vm.updateBiography(text) { ok, error ->
                    if (!isAdded) return@updateBiography
                    if (ok) {
                        Toast.makeText(requireContext(), R.string.msg_biography_updated, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), error ?: "No se pudo guardar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CompletedAdapter(private var items: List<String>) :
    RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    inner class CompletedViewHolder(val binding: ItemCompletedCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.tvCompletedTitle.text = title
        }
    }

    fun updateList(newItems: List<String>) {
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