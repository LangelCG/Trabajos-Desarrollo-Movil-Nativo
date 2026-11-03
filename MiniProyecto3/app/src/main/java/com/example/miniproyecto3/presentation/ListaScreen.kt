package com.example.miniproyecto3.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.miniproyecto3.data.Publicacion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    publicaciones: List<Publicacion>,
    onAgregar: () -> Unit,
    onEliminar: (Publicacion) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Red Social Local") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Nueva publicacion")
            }
        }
    ) { padding ->
        if (publicaciones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay publicaciones todavia.\n¡Crea la primera con el botón +!",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(publicaciones, key = { it.id }) { pub ->
                    ItemPublicacion(pub, onEliminar)
                }
            }
        }
    }
}

@Composable
private fun ItemPublicacion(pub: Publicacion, onEliminar: (Publicacion) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            // Foto de la publicacion (cargada desde el almacenamiento de la app con Coil)
            if (pub.fotoUri.isNotEmpty()) {
                AsyncImage(
                    model = pub.fotoUri,
                    contentDescription = pub.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
            } else {
                Box(
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Image, contentDescription = "Sin foto") }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(pub.titulo, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                if (pub.descripcion.isNotEmpty()) {
                    Text(pub.descripcion, style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Text(formatoFecha.format(Date(pub.fecha)),
                    style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            IconButton(onClick = { onEliminar(pub) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                    tint = Color.Gray)
            }
        }
    }
}
