package com.example.p3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etDato = findViewById<EditText>(R.id.etDato)
        val btnIr = findViewById<Button>(R.id.btnIr)

        btnIr.setOnClickListener {
            val texto = etDato.text.toString()
            val intent = Intent(this, p32::class.java)
            intent.putExtra("DATO_ENVIADO", texto)
            startActivity(intent)
        }
    }
}