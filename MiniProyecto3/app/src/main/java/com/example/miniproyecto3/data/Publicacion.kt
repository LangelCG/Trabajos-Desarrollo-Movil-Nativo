package com.example.miniproyecto3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad de la base de datos: una publicacion de la red social local
@Entity(tableName = "publicaciones")
data class Publicacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val fotoUri: String = "",     // ruta file:// de la foto copiada al almacenamiento de la app
    val fecha: Long = System.currentTimeMillis()  // marca de tiempo (manejo de fechas)
)
