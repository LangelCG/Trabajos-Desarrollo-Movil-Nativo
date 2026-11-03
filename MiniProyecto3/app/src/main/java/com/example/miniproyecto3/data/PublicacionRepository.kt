package com.example.miniproyecto3.data

import kotlinx.coroutines.flow.Flow

// Repositorio: unico punto de acceso a los datos (MVVM)
class PublicacionRepository(private val dao: PublicacionDao) {
    val todas: Flow<List<Publicacion>> = dao.obtenerTodas()

    suspend fun guardar(publicacion: Publicacion) = dao.insertar(publicacion)

    suspend fun eliminar(publicacion: Publicacion) = dao.eliminar(publicacion)
}
