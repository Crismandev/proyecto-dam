package pe.edu.cibertec.techmentor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val bottomNavigation =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorHome,
                    HomeFragment()
                )
                .commit()
        }

        bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navInicio -> {
                    mostrarFragmento(HomeFragment())
                    true
                }

                R.id.navCursos -> {
                    mostrarFragmento(CursosFragment())
                    true
                }

                R.id.navActividades -> {
                    mostrarFragmento(EntrevistasFragment())
                    true
                }

                R.id.navPerfil -> {
                    mostrarFragmento(PerfilFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun mostrarFragmento(fragmento: androidx.fragment.app.Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.contenedorHome,
                fragmento
            )
            .commit()
    }
}