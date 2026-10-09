package pe.edu.cibertec.techmentor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CursoAdapter(
    private val cursos: List<Curso>,
    private val onCursoClick: (Curso) -> Unit
) : RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CursoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_curso,
                parent,
                false
            )

        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CursoViewHolder,
        position: Int
    ) {

        val curso = cursos[position]

        holder.tvNombreCurso.text = curso.nombre
        holder.tvDescripcionCurso.text = curso.descripcion

        Glide.with(holder.itemView.context)
            .load(curso.imagenUrl)
            .centerCrop()
            .into(holder.ivCurso)

        holder.btnVerCurso.setOnClickListener {
            onCursoClick(curso)
        }
    }

    override fun getItemCount(): Int {
        return cursos.size
    }

    class CursoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivCurso: ImageView =
            itemView.findViewById(R.id.ivCurso)

        val tvNombreCurso: TextView =
            itemView.findViewById(R.id.tvNombreCurso)

        val tvDescripcionCurso: TextView =
            itemView.findViewById(R.id.tvDescripcionCurso)

        val btnVerCurso: Button =
            itemView.findViewById(R.id.btnVerCurso)
    }
}