package com.example.practica6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EstudianteAdapter(
    private val lista: List<Estudiante>
) : RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {

    class EstudianteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCarrera: TextView = itemView.findViewById(R.id.tvCarrera)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudiante, parent, false)
        return EstudianteViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = lista[position]
        holder.tvNombre.text = estudiante.nombre
        holder.tvCarrera.text = estudiante.carrera

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Seleccionaste a: ${estudiante.nombre}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = lista.size
}