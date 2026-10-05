package com.example.p3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class p32 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p32)

        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val datoRecibido = intent.getStringExtra("DATO_ENVIADO") ?: "Sin datos"
        tvResultado.text = datoRecibido
    }
}