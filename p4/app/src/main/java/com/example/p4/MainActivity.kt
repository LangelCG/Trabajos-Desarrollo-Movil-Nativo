package com.example.p4

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var generoSeleccionado = ""
    var deportesMarcado = false
    var musicaMarcada = false
    var notificacionesActivas = false
    var valorSeekBar = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinnerOpciones)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupGenero)
        val checkDeportes = findViewById<CheckBox>(R.id.checkDeportes)
        val checkMusica = findViewById<CheckBox>(R.id.checkMusica)
        val switchNotif = findViewById<Switch>(R.id.switchNotificaciones)
        val seekBar = findViewById<SeekBar>(R.id.seekSatisfaccion)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)
        val tvResumen = findViewById<TextView>(R.id.tvResumen)

        // --- Spinner: llenar opciones ---
        val opciones = arrayOf("Selecciona una opción", "Opción A", "Opción B", "Opción C")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = adapter

        var opcionSpinner = opciones[0]
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                opcionSpinner = opciones[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- RadioGroup ---
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            generoSeleccionado = when (checkedId) {
                R.id.radioMasculino -> "Masculino"
                R.id.radioFemenino -> "Femenino"
                else -> ""
            }
        }

        checkDeportes.setOnCheckedChangeListener { _, isChecked -> deportesMarcado = isChecked }
        checkMusica.setOnCheckedChangeListener { _, isChecked -> musicaMarcada = isChecked }

        switchNotif.setOnCheckedChangeListener { _, isChecked -> notificacionesActivas = isChecked }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                valorSeekBar = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnProcesar.setOnClickListener {
            val dia = datePicker.dayOfMonth
            val mes = datePicker.month + 1
            val anio = datePicker.year

            val intereses = mutableListOf<String>()
            if (deportesMarcado) intereses.add("Deportes")
            if (musicaMarcada) intereses.add("Música")

            val resumen = """
                Opción elegida: $opcionSpinner
                Género: $generoSeleccionado
                Intereses: ${if (intereses.isEmpty()) "Ninguno" else intereses.joinToString(", ")}
                Notificaciones: ${if (notificacionesActivas) "Activadas" else "Desactivadas"}
                Satisfacción: $valorSeekBar%
                Fecha seleccionada: $dia/$mes/$anio
            """.trimIndent()

            tvResumen.text = resumen
        }
    }
}