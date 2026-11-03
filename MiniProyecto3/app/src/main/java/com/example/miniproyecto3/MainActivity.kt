package com.example.miniproyecto3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miniproyecto3.presentation.FormularioScreen
import com.example.miniproyecto3.presentation.ListaScreen
import com.example.miniproyecto3.presentation.PublicacionViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PublicacionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val publicaciones by viewModel.publicaciones.collectAsState()

            NavHost(navController = navController, startDestination = "lista") {
                composable("lista") {
                    ListaScreen(
                        publicaciones = publicaciones,
                        onAgregar = { navController.navigate("formulario") },
                        onEliminar = { viewModel.eliminar(it) }
                    )
                }
                composable("formulario") {
                    FormularioScreen(
                        onGuardar = { titulo, descripcion, fotoUri, fecha ->
                            viewModel.guardar(titulo, descripcion, fotoUri, fecha)
                            navController.popBackStack()
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
