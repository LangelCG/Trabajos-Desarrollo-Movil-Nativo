package com.example.practica8

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EstudianteViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).estudianteDao()

    val estudiantes: Flow<List<Estudiante>> = dao.obtenerTodos()

    fun insertar(nombre: String, carrera: String) {
        viewModelScope.launch { dao.insertar(Estudiante(nombre = nombre, carrera = carrera)) }
    }

    fun actualizar(estudiante: Estudiante) {
        viewModelScope.launch { dao.actualizar(estudiante) }
    }

    fun eliminar(estudiante: Estudiante) {
        viewModelScope.launch { dao.eliminar(estudiante) }
    }
}