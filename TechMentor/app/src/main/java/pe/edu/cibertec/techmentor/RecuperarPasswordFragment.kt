package pe.edu.cibertec.techmentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecuperarPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_recuperar_password,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvVolverLoginRecuperacion =
            view.findViewById<TextView>(
                R.id.tvVolverLoginRecuperacion
            )

        tvVolverLoginRecuperacion.setOnClickListener {

            parentFragmentManager.popBackStack()
        }
    }
}