package com.example.p5

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCargar = findViewById<Button>(R.id.btnCargar)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val prefs = getSharedPreferences("MisDatos", Context.MODE_PRIVATE)

        btnGuardar.setOnClickListener {
            prefs.edit()
                .putString("usuario", etUsuario.text.toString())
                .putString("password", etPassword.text.toString())
                .apply()
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
        }

        btnCargar.setOnClickListener {
            val usuario = prefs.getString("usuario", "") ?: ""
            val password = prefs.getString("password", "") ?: ""
            etUsuario.setText(usuario)
            etPassword.setText(password)
            tvResultado.text = if (usuario.isEmpty() && password.isEmpty()) {
                "No hay datos guardados"
            } else {
                "Usuario: $usuario\nContraseña: $password"
            }
        }

        btnBorrar.setOnClickListener {
            prefs.edit().clear().apply()
            etUsuario.setText("")
            etPassword.setText("")
            tvResultado.text = "Datos borrados"
            Toast.makeText(this, "Datos borrados", Toast.LENGTH_SHORT).show()
        }

        val usuarioGuardado = prefs.getString("usuario", "") ?: ""
        val passwordGuardado = prefs.getString("password", "") ?: ""
        if (usuarioGuardado.isNotEmpty() || passwordGuardado.isNotEmpty()) {
            etUsuario.setText(usuarioGuardado)
            etPassword.setText(passwordGuardado)
            tvResultado.text = "Datos recuperados al abrir:\nUsuario: $usuarioGuardado\nContraseña: $passwordGuardado"
        }
    }
}