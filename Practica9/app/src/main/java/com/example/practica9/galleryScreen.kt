package com.example.practica9

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

/**
 * Práctica 09 — Galería de imágenes.
 *
 * - Lista de URLs de prueba (picsum.photos genera fotos variadas y estables).
 * - LazyVerticalGrid arma la cuadrícula adaptativa.
 * - AsyncImage (Coil) descarga cada imagen de forma asíncrona, la guarda en caché
 *   y muestra los estados de carga (placeholder) y de error.
 * - Reto: al tocar una imagen se abre un diálogo con la imagen ampliada.
 */
val imagenesDePrueba: List<String> = List(18) { i ->
    "https://picsum.photos/seed/practica09$i/600/600"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    // Estado del reto: la imagen seleccionada (null = sin diálogo)
    var imagenSeleccionada by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_galeria)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(imagenesDePrueba) { url ->
                ImagenItem(
                    url = url,
                    onClick = { imagenSeleccionada = url }
                )
            }
        }
    }

    // Vista de detalle: diálogo con la imagen en grande al centro de la pantalla
    imagenSeleccionada?.let { url ->
        DialogoDetalle(url = url, onCerrar = { imagenSeleccionada = null })
    }
}

/** Celda de la cuadrícula: imagen con placeholder de carga y estado de error. */
@Composable
fun ImagenItem(url: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Imagen de la galería",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            // Placeholder: mientras la imagen se descarga
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp),
                    strokeWidth = 3.dp
                )
            },
            // Error: si la URL falla o no hay red
            error = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error al cargar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(28.dp)
                    )
                }
            }
        )
    }
}

/** Detalle: diálogo emergente con la imagen seleccionada en tamaño grande. */
@Composable
fun DialogoDetalle(url: String, onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Imagen ampliada") },
        text = {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = "Imagen ampliada",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error al cargar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onCerrar) { Text("Cerrar") }
        }
    )
}
