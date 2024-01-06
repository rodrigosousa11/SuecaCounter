package com.example.suecacounter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var equipeA: String
    private lateinit var equipeB: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_names, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val editTextEquipeA: EditText = dialogView.findViewById(R.id.editTextEquipaA)
                val editTextEquipeB: EditText = dialogView.findViewById(R.id.editTextEquipaB)
                equipeA = editTextEquipeA.text.toString()
                equipeB = editTextEquipeB.text.toString()

                // Iniciar a CounterActivity passando os nomes das equipes
                val intent = Intent(this, Counter::class.java)
                intent.putExtra("nomeEquipeA", equipeA)
                intent.putExtra("nomeEquipeB", equipeB)
                startActivity(intent)

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
