package com.example.registroestudiantes

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FormularioScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("registro_prefs", Context.MODE_PRIVATE) }

    // El estado inicial se lee de SharedPreferences (persistencia)
    var matricula by remember { mutableStateOf(prefs.getString("matricula", "") ?: "") }
    var nombre by remember { mutableStateOf(prefs.getString("nombre", "") ?: "") }
    var carrera by remember { mutableStateOf(prefs.getString("carrera", "") ?: "") }
    var turno by remember { mutableStateOf(prefs.getString("turno", "Matutino") ?: "Matutino") }
    var activo by remember { mutableStateOf(prefs.getBoolean("activo", true)) }
    var menuAbierto by remember { mutableStateOf(false) }

    val carreras = listOf(
        "Ing. en Sistemas",
        "Ing. Industrial",
        "Lic. en Administración",
        "Lic. en Contaduría",
        "Ing. Civil"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registro de Estudiantes", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Menú desplegable de carrera
        Text("Carrera")
        Box {
            OutlinedButton(
                onClick = { menuAbierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (carrera.isEmpty()) "Selecciona una carrera" else carrera)
            }
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                carreras.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            carrera = opcion
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        // Turno con RadioButtons
        Text("Turno")
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf("Matutino", "Vespertino").forEach { opcion ->
                RadioButton(
                    selected = turno == opcion,
                    onClick = { turno = opcion }
                )
                Text(opcion, modifier = Modifier.padding(end = 16.dp))
            }
        }

        // Estatus con Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Estatus: ${if (activo) "Activo" else "Inactivo"}",
                modifier = Modifier.weight(1f)
            )
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        Button(
            onClick = {
                if (matricula.isBlank() || nombre.isBlank() || carrera.isEmpty()) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    // Guardar en SharedPreferences
                    prefs.edit()
                        .putString("matricula", matricula)
                        .putString("nombre", nombre)
                        .putString("carrera", carrera)
                        .putString("turno", turno)
                        .putBoolean("activo", activo)
                        .apply()

                    val estatus = if (activo) "Activo" else "Inactivo"
                    navController.navigate(
                        "confirmacion/${Uri.encode(matricula)}/${Uri.encode(nombre)}/" +
                                "${Uri.encode(carrera)}/${Uri.encode(turno)}/${Uri.encode(estatus)}"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
    }
}