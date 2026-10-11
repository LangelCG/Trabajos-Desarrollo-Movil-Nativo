package com.example.practica8

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ENTIDAD: representa la tabla "estudiantes"
@Entity(tableName = "estudiantes")
data class Estudiante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val carrera: String
)

// DAO: operaciones CRUD
@Dao
interface EstudianteDao {
    @Insert
    suspend fun insertar(estudiante: Estudiante)

    @Query("SELECT * FROM estudiantes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Estudiante>>

    @Update
    suspend fun actualizar(estudiante: Estudiante)

    @Delete
    suspend fun eliminar(estudiante: Estudiante)
}

// BASE DE DATOS con patrón Singleton
@Database(entities = [Estudiante::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun estudianteDao(): EstudianteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "estudiantes_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}