package com.example.practica7

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var tvEstado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvEstado = findViewById(R.id.tvEstado)
        val btnPopup = findViewById<Button>(R.id.btnPopup)

        btnPopup.setOnClickListener { vista ->
            val popup = PopupMenu(this, vista)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_editar -> {
                        tvEstado.text = "Seleccionaste: Editar"
                        true
                    }
                    R.id.popup_compartir -> {
                        tvEstado.text = "Seleccionaste: Compartir"
                        true
                    }
                    R.id.popup_eliminar -> {
                        tvEstado.text = "Seleccionaste: Eliminar"
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_buscar -> {
                tvEstado.text = "Presionaste: Buscar"
                Toast.makeText(this, "Buscar", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_favoritos -> {
                tvEstado.text = "Presionaste: Favoritos"
                Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_ajustes -> {
                tvEstado.text = "Presionaste: Ajustes"
                return true
            }
            R.id.action_acerca -> {
                tvEstado.text = "Presionaste: Acerca de"
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}