package com.example.miniproyecto3.presentation

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    onGuardar: (titulo: String, descripcion: String, fotoUri: String, fecha: Long) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    // rememberSaveable: el formulario sobrevive si el sistema recrea la actividad
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var fotoUri by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var error by remember { mutableStateOf<String?>(null) }
    var mostrarPicker by remember { mutableStateOf(false) }

    val formatoFecha = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // --- Camara: archivo temporal donde se guarda la foto (ruta como String
    //     para que sobreviva una recreacion de la actividad) ---
    var rutaFoto by rememberSaveable { mutableStateOf("") }

    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            val file = rutaFoto.takeIf { it.isNotEmpty() }?.let { File(it) }
            if (file != null) fotoUri = copiarFoto(context, Uri.fromFile(file)) ?: fotoUri
        }
    }

    fun lanzarCamara() {
        val dir = File(context.cacheDir, "camara").apply { mkdirs() }
        val file = File(dir, "foto_${System.currentTimeMillis()}.jpg")
        rutaFoto = file.absolutePath
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        camaraLauncher.launch(uri)
    }

    // Permiso de camara (gestion de permisos del sistema)
    val permisoCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) lanzarCamara()
        else error = "Permiso de camara denegado"
    }

    // --- Galeria: selector de imagenes (Photo Picker, no pide permisos) ---
    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) fotoUri = copiarFoto(context, uri) ?: fotoUri
    }

    fun tomarFoto() {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (concedido) lanzarCamara() else permisoCamara.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva publicacion") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Titulo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripcion") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Fecha: se genera automaticamente y puede seleccionarse ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fecha: ${formatoFecha.format(Date(fecha))}",
                    modifier = Modifier.weight(1f))
                OutlinedButton(onClick = { mostrarPicker = true }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Cambiar")
                }
            }

            if (mostrarPicker) {
                val estado = rememberDatePickerState(initialSelectedDateMillis = fecha)
                DatePickerDialog(
                    onDismissRequest = { mostrarPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            estado.selectedDateMillis?.let {
                                // mediodia UTC para que la fecha local sea correcta
                                fecha = it + 12L * 60 * 60 * 1000
                            }
                            mostrarPicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarPicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = estado) }
            }

            // --- Foto: camara o galeria ---
            Text("Fotografia", style = MaterialTheme.typography.titleSmall)

            if (fotoUri.isNotEmpty()) {
                AsyncImage(
                    model = fotoUri,
                    contentDescription = "Foto seleccionada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                        .clip(RoundedCornerShape(10.dp)).background(Color.LightGray)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                        .clip(RoundedCornerShape(10.dp)).background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) { Text("Sin fotografia", color = Color.DarkGray) }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { tomarFoto() }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Camara")
                }
                OutlinedButton(
                    onClick = {
                        galeriaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Galeria")
                }
            }

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    if (titulo.isBlank()) error = "El titulo es obligatorio"
                    else {
                        error = null
                        onGuardar(titulo, descripcion, fotoUri, fecha)
                    }
                },
                enabled = titulo.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar publicacion") }
        }
    }
}

// Copia la imagen elegida al almacenamiento de la app para que persista en Room
private fun copiarFoto(context: Context, origen: Uri): String? {
    return try {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val destino = File(dir, "img_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(origen)?.use { entrada ->
            destino.outputStream().use { salida -> entrada.copyTo(salida) }
        } ?: return null
        Uri.fromFile(destino).toString()
    } catch (e: Exception) {
        null
    }
}
