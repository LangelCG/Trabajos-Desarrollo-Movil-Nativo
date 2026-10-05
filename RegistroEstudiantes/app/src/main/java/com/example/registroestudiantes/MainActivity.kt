package com.example.registroestudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavegacion()
                }
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "formulario") {
        composable("formulario") {
            FormularioScreen(navController)
        }
        composable(
            route = "confirmacion/{matricula}/{nombre}/{carrera}/{turno}/{estatus}",
            arguments = listOf(
                navArgument("matricula") { type = NavType.StringType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("carrera") { type = NavType.StringType },
                navArgument("turno") { type = NavType.StringType },
                navArgument("estatus") { type = NavType.StringType }
            )
        ) { entry ->
            ConfirmacionScreen(
                navController = navController,
                matricula = entry.arguments?.getString("matricula") ?: "",
                nombre = entry.arguments?.getString("nombre") ?: "",
                carrera = entry.arguments?.getString("carrera") ?: "",
                turno = entry.arguments?.getString("turno") ?: "",
                estatus = entry.arguments?.getString("estatus") ?: ""
            )
        }
    }
}