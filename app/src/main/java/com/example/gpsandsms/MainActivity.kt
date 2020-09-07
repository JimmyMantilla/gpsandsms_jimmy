package com.example.gpsandsms

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var ed_numero: EditText
    private lateinit var button: Button
    private lateinit var rb_1: RadioButton
    private lateinit var rb_2: RadioButton
    private lateinit var rb_3: RadioButton
    var seleccionintervalo: String = "0"
    var telefono: String = ""
    var spinnerArray = arrayOf("30seg", "1min", "2min", "5min", "10min")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ed_numero = findViewById<EditText>(R.id.ed_numero)
        button = findViewById<Button>(R.id.button)
        rb_1 = findViewById<RadioButton>(R.id.rb_1)
        rb_2 = findViewById<RadioButton>(R.id.rb_2)
        rb_3 = findViewById<RadioButton>(R.id.rb_3)
        rb_1.setOnClickListener() {
            seleccionintervalo = "30000"
        }
        rb_2.setOnClickListener() {
            seleccionintervalo = "120000"
        }
        rb_3.setOnClickListener() {
            seleccionintervalo = "300000"
        }
        button.setOnClickListener {
            telefono = ed_numero.text.toString()
            if (seleccionintervalo == "0" || telefono == "") {
                alertdialog()
            } else {
                enviardatos()
            }
        }
    }

    private fun enviardatos() {
        startActivity(
                Intent(this, MapsActivity::class.java).putExtra("seleccionintervalo", seleccionintervalo)
                        .putExtra("telefono", telefono)
        )
    }

    private fun alertdialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ATENCIÓN!")
        builder.setMessage("DEBE LLENAR EL NUMERO DE TELEFÓNO Y ADEMÁS SELECCIONAR EL INTERVALO DE TIEMPO!!")
        builder.setPositiveButton("OK") { dialog, which -> }
        builder.show()
    }
}