package com.example.miniproyecto2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: TareaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaTareas(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareaViewModel) {
    val tareas by vm.tareas.collectAsState(initial = emptyList())
    val filtro by vm.filtro.collectAsState()
    val orden by vm.orden.collectAsState()

    var menuAbierto by remember { mutableStateOf(false) }
    var dialogoAbierto by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf<Tarea?>(null) }
    var texto by remember { mutableStateOf("") }

    val formatoFecha = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { vm.eliminarCompletadas() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar completadas")
                    }
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        Text(
                            "Filtrar",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        Filtro.values().forEach { f ->
                            DropdownMenuItem(
                                text = { Text((if (filtro == f) "✓ " else "") + f.etiqueta) },
                                onClick = {
                                    vm.filtro.value = f
                                    menuAbierto = false
                                }
                            )
                        }
                        HorizontalDivider()
                        Text(
                            "Ordenar",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        Orden.values().forEach { o ->
                            DropdownMenuItem(
                                text = { Text((if (orden == o) "✓ " else "") + o.etiqueta) },
                                onClick = {
                                    vm.orden.value = o
                                    menuAbierto = false
                                }
                            )
                        }
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Borrar completadas") },
                            onClick = {
                                vm.eliminarCompletadas()
                                menuAbierto = false
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editando = null
                texto = ""
                dialogoAbierto = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                "Filtro: ${filtro.etiqueta}  ·  Orden: ${orden.etiqueta}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (tareas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tareas. Presiona + para agregar.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tareas, key = { it.id }) { tarea ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = tarea.completada,
                                    onCheckedChange = {
                                        vm.actualizar(tarea.copy(completada = it))
                                    }
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        tarea.titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        textDecoration = if (tarea.completada)
                                            TextDecoration.LineThrough else TextDecoration.None
                                    )
                                    Text(
                                        formatoFecha.format(Date(tarea.fecha)),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(onClick = {
                                    editando = tarea
                                    texto = tarea.titulo
                                    dialogoAbierto = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = { vm.eliminar(tarea) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para agregar o editar
    if (dialogoAbierto) {
        AlertDialog(
            onDismissRequest = { dialogoAbierto = false },
            title = { Text(if (editando == null) "Nueva tarea" else "Editar tarea") },
            text = {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("Título") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (texto.isNotBlank()) {
                        val actual = editando
                        if (actual == null) {
                            vm.insertar(texto.trim())
                        } else {
                            vm.actualizar(actual.copy(titulo = texto.trim()))
                        }
                        dialogoAbierto = false
                    }
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { dialogoAbierto = false }) { Text("Cancelar") }
            }
        )
    }
}