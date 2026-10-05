package com.example.p2_calc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var PrimerNumero = 0.0
    var Operador = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pantalla = findViewById<TextView>(R.id.texto)
        val botonSuma = findViewById<Button>(R.id.bMas)
        val botonResta = findViewById<Button>(R.id.bRes)
        val botonMultiplicacion = findViewById<Button>(R.id.bMulti)
        val botonDivision = findViewById<Button>(R.id.bDiv)
        val botonIgual = findViewById<Button>(R.id.bIgu)
        val botonLimpiar = findViewById<Button>(R.id.bAc)
        val boton0 = findViewById<Button>(R.id.b0)
        val boton1 = findViewById<Button>(R.id.b1)
        val boton2 = findViewById<Button>(R.id.b2)
        val boton3 = findViewById<Button>(R.id.b3)
        val boton4 = findViewById<Button>(R.id.b4)
        val boton5 = findViewById<Button>(R.id.b5)
        val boton6 = findViewById<Button>(R.id.b6)
        val boton7 = findViewById<Button>(R.id.b7)
        val boton8 = findViewById<Button>(R.id.b8)
        val boton9 = findViewById<Button>(R.id.b9)

        // --- Botones numéricos: cada uno concatena su dígito a la pantalla ---
        boton0.setOnClickListener { pantalla.text = pantalla.text.toString() + "0" }
        boton1.setOnClickListener { pantalla.text = pantalla.text.toString() + "1" }
        boton2.setOnClickListener { pantalla.text = pantalla.text.toString() + "2" }
        boton3.setOnClickListener { pantalla.text = pantalla.text.toString() + "3" }
        boton4.setOnClickListener { pantalla.text = pantalla.text.toString() + "4" }
        boton5.setOnClickListener { pantalla.text = pantalla.text.toString() + "5" }
        boton6.setOnClickListener { pantalla.text = pantalla.text.toString() + "6" }
        boton7.setOnClickListener { pantalla.text = pantalla.text.toString() + "7" }
        boton8.setOnClickListener { pantalla.text = pantalla.text.toString() + "8" }
        boton9.setOnClickListener { pantalla.text = pantalla.text.toString() + "9" }

        // --- Botones de operador: guardan el primer número y el operador, limpian pantalla ---
        botonSuma.setOnClickListener {
            PrimerNumero = pantalla.text.toString().toDoubleOrNull() ?: 0.0
            Operador = "+"
            pantalla.text = ""
        }
        botonResta.setOnClickListener {
            PrimerNumero = pantalla.text.toString().toDoubleOrNull() ?: 0.0
            Operador = "-"
            pantalla.text = ""
        }
        botonMultiplicacion.setOnClickListener {
            PrimerNumero = pantalla.text.toString().toDoubleOrNull() ?: 0.0
            Operador = "*"
            pantalla.text = ""
        }
        botonDivision.setOnClickListener {
            PrimerNumero = pantalla.text.toString().toDoubleOrNull() ?: 0.0
            Operador = "/"
            pantalla.text = ""
        }

        // --- Botón "=": lee el segundo número, calcula según el operador guardado ---
        botonIgual.setOnClickListener {
            val segundoNumero = pantalla.text.toString().toDoubleOrNull() ?: 0.0
            val resultado = when (Operador) {
                "+" -> PrimerNumero + segundoNumero
                "-" -> PrimerNumero - segundoNumero
                "*" -> PrimerNumero * segundoNumero
                "/" -> if (segundoNumero != 0.0) PrimerNumero / segundoNumero else Double.NaN
                else -> 0.0
            }
            pantalla.text = if (resultado.isNaN()) "Error" else resultado.toString()
        }

        // --- Botón "C": resetea todo ---
        botonLimpiar.setOnClickListener {
            pantalla.text = ""
            PrimerNumero = 0.0
            Operador = ""
        }
    }
}