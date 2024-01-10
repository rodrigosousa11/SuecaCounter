package com.example.suecacounter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Ganhou : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganhou)

        val vencedor = intent.getStringExtra("vencedor")

        val mensagemTextView = findViewById<TextView>(R.id.textViewEquipaA)

        val mensagem: String = when (vencedor) {
            null -> "Vencedor indefinido"
            "Empate" -> "As equipas empataram!"
            else -> if (vencedor.startsWith("Equipa")) {
                "$vencedor ganhou!"
            } else {
                "Equipa $vencedor ganhou!"
            }
        }

        mensagemTextView.text = mensagem

        val btnVoltarAtras = findViewById<Button>(R.id.button)

        btnVoltarAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
