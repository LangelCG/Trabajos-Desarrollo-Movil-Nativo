package com.example.practica6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val listaEstudiantes = listOf(
            Estudiante("Juan Pérez", "Ing. en Sistemas"),
            Estudiante("María López", "Ing. Industrial"),
            Estudiante("Carlos Ruiz", "Lic. en Administración"),
            Estudiante("Ana Torres", "Ing. Civil"),
            Estudiante("Luis Gómez", "Lic. en Contaduría"),
            Estudiante("Sofía Reyes", "Ing. en Sistemas"),
            Estudiante("Diego Castro", "Ing. Industrial"),
            Estudiante("Valeria Ortiz", "Lic. en Administración")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EstudianteAdapter(listaEstudiantes)
    }
}