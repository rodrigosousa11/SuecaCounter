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

        // Recupere o valor do vencedor do Intent
        val vencedor = intent.getStringExtra("vencedor")

        // Encontre o TextView na layout
        val mensagemTextView = findViewById<TextView>(R.id.textViewEquipaA)

        // Verifique se o vencedor não é nulo antes de definir a mensagem
        val mensagem: String = when (vencedor) {
            null -> "Vencedor indefinido"
            "Empate" -> "Empatou"
            else -> "$vencedor ganhou"
        }

        // Defina a mensagem no TextView
        mensagemTextView.text = mensagem

        // Encontre o botão na layout
        val btnVoltarAtras = findViewById<Button>(R.id.button)

        // Adicione um listener para o botão
        btnVoltarAtras.setOnClickListener {
            // Iniciar a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a atividade atual (Ganhou)
        }
    }
}
