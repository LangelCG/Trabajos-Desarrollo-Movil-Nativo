package com.example.miniproyecto3.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproyecto3.data.AppDatabase
import com.example.miniproyecto3.data.Publicacion
import com.example.miniproyecto3.data.PublicacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PublicacionViewModel(app: Application) : AndroidViewModel(app) {

    private val repository =
        PublicacionRepository(AppDatabase.getDatabase(app).publicacionDao())

    // Lista reactiva de publicaciones (se actualiza sola al guardar/borrar)
    val publicaciones: StateFlow<List<Publicacion>> = repository.todas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardar(titulo: String, descripcion: String, fotoUri: String, fecha: Long) {
        viewModelScope.launch {
            repository.guardar(
                Publicacion(
                    titulo = titulo.trim(),
                    descripcion = descripcion.trim(),
                    fotoUri = fotoUri,
                    fecha = fecha
                )
            )
        }
    }

    fun eliminar(publicacion: Publicacion) {
        viewModelScope.launch { repository.eliminar(publicacion) }
    }
}
