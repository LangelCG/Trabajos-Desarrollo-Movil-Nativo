package com.example.miniproyecto2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class Filtro(val etiqueta: String) {
    TODAS("Todas"),
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas")
}

enum class Orden(val etiqueta: String) {
    FECHA("Por fecha"),
    ESTADO("Por estado")
}

class TareaViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).tareaDao()

    val filtro = MutableStateFlow(Filtro.TODAS)
    val orden = MutableStateFlow(Orden.FECHA)

    // Combina la base de datos + filtro + orden en una sola lista reactiva
    val tareas: Flow<List<Tarea>> = combine(dao.obtenerTodas(), filtro, orden) { lista, f, o ->
        val filtradas = when (f) {
            Filtro.TODAS -> lista
            Filtro.PENDIENTES -> lista.filter { !it.completada }
            Filtro.COMPLETADAS -> lista.filter { it.completada }
        }
        when (o) {
            Orden.FECHA -> filtradas.sortedByDescending { it.fecha }
            Orden.ESTADO -> filtradas.sortedBy { it.completada }
        }
    }

    fun insertar(titulo: String) {
        viewModelScope.launch { dao.insertar(Tarea(titulo = titulo)) }
    }

    fun actualizar(tarea: Tarea) {
        viewModelScope.launch { dao.actualizar(tarea) }
    }

    fun eliminar(tarea: Tarea) {
        viewModelScope.launch { dao.eliminar(tarea) }
    }

    fun eliminarCompletadas() {
        viewModelScope.launch { dao.eliminarCompletadas() }
    }
}