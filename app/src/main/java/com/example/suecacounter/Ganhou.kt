package com.example.suecacounter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.suecacounter.databinding.ActivityGanhouBinding

class Ganhou : AppCompatActivity() {
    private lateinit var binding: ActivityGanhouBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGanhouBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vencedor = intent.getStringExtra("vencedor")

        val mensagemTextView = binding.textViewEquipaA

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

        val btnVoltarAtras = binding.button

        btnVoltarAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
