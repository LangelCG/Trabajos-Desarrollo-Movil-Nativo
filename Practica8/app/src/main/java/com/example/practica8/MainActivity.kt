package com.example.practica8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val viewModel: EstudianteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaEstudiantes(viewModel)
                }
            }
        }
    }
}

@Composable
fun PantallaEstudiantes(viewModel: EstudianteViewModel) {
    // Estado reactivo: se actualiza solo cuando cambia la base de datos
    val lista by viewModel.estudiantes.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<Estudiante?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Estudiantes (Room)", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = carrera,
            onValueChange = { carrera = it },
            label = { Text("Carrera") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && carrera.isNotBlank()) {
                        val actual = editando
                        if (actual == null) {
                            viewModel.insertar(nombre, carrera)
                        } else {
                            viewModel.actualizar(actual.copy(nombre = nombre, carrera = carrera))
                            editando = null
                        }
                        nombre = ""
                        carrera = ""
                    }
                }
            ) {
                Text(if (editando == null) "Agregar" else "Actualizar")
            }

            if (editando != null) {
                OutlinedButton(onClick = {
                    editando = null
                    nombre = ""
                    carrera = ""
                }) { Text("Cancelar") }
            }
        }

        HorizontalDivider()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lista, key = { it.id }) { estudiante ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(estudiante.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(estudiante.carrera, style = MaterialTheme.typography.bodyMedium)
                        }
                        TextButton(onClick = {
                            editando = estudiante
                            nombre = estudiante.nombre
                            carrera = estudiante.carrera
                        }) { Text("Editar") }
                        TextButton(onClick = { viewModel.eliminar(estudiante) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}