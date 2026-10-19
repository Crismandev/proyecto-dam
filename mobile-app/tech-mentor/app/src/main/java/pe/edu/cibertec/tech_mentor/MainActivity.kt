package pe.edu.cibertec.tech_mentor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import pe.edu.cibertec.tech_mentor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val tabIds = setOf(
            R.id.homeFragment,
            R.id.categoryFragment,
            R.id.practiceFragment,
            R.id.profileFragment
        )

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == navController.currentDestination?.id) {
                return@setOnItemSelectedListener true
            }
            val restored = navController.popBackStack(item.itemId, false)
            if (!restored) {
                navController.navigate(
                    item.itemId,
                    null,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.homeFragment, false)
                        .build()
                )
            }
            true
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            navController.popBackStack(item.itemId, false)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val tabId = when (destination.id) {
                R.id.categoryDetailsFragment -> R.id.categoryFragment
                R.id.settingsFragment, R.id.editProfileFragment -> R.id.profileFragment
                in tabIds -> destination.id
                else -> return@addOnDestinationChangedListener
            }
            if (binding.bottomNav.selectedItemId != tabId) {
                binding.bottomNav.menu.findItem(tabId)?.isChecked = true
            }
        }
    }

    fun selectTab(itemId: Int) {
        if (binding.bottomNav.selectedItemId != itemId) {
            binding.bottomNav.selectedItemId = itemId
        }
    }
}
