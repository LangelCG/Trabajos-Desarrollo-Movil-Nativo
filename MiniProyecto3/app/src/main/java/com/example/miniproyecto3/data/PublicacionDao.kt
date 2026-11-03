package com.example.miniproyecto3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionDao {
    @Insert
    suspend fun insertar(publicacion: Publicacion)

    // Lectura reactiva: la lista se actualiza sola cuando cambia la tabla
    @Query("SELECT * FROM publicaciones ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<Publicacion>>

    @Delete
    suspend fun eliminar(publicacion: Publicacion)
}
