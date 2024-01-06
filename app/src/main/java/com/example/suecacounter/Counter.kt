package com.example.suecacounter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Counter : AppCompatActivity() {
    private lateinit var btnEquipaA: Button
    private lateinit var btnEquipaB: Button
    private lateinit var btnTerminar: Button
    private lateinit var textViewEquipaA: TextView
    private lateinit var textViewEquipaB: TextView

    private var pontosEquipaA = 0
    private var pontosEquipaB = 0
    private var pontosJogadaAtual = 0
    private var cartasSelecionadas = 0
    private val maxCartasSelecionadas = 4

    private lateinit var imagemAs: ImageView
    private lateinit var imagemBisca: ImageView
    private lateinit var imagemRei: ImageView
    private lateinit var imagemValete: ImageView
    private lateinit var imagemDama: ImageView

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        textViewEquipaA = findViewById(R.id.textViewEquipaA)
        textViewEquipaB = findViewById(R.id.textViewEquipaB)
        btnEquipaA = findViewById(R.id.btnEquipaA)
        btnEquipaB = findViewById(R.id.btnEquipaB)
        btnTerminar = findViewById(R.id.btnTerminar)

        imagemAs = findViewById(R.id.imagemas)
        imagemBisca = findViewById(R.id.imagemBisca)
        imagemRei = findViewById(R.id.imagemRei)
        imagemValete = findViewById(R.id.imagemValete)
        imagemDama = findViewById(R.id.imagemDama)

        val nomeEquipeA = intent.getStringExtra("nomeEquipeA")
        val nomeEquipeB = intent.getStringExtra("nomeEquipeB")

        val textViewNomeEquipeA = findViewById<TextView>(R.id.textViewEquipaA)
        val textViewNomeEquipeB = findViewById<TextView>(R.id.textViewEquipaB)
        textViewNomeEquipeA.text = "$nomeEquipeA: $pontosEquipaA"
        textViewNomeEquipeB.text = "$nomeEquipeB: $pontosEquipaB"

        val cartas = listOf(11, 10, 4, 3, 2)

        val imagensCarta = listOf(imagemAs, imagemBisca, imagemRei, imagemValete, imagemDama)
        for ((index, imagemCarta) in imagensCarta.withIndex()) {
            imagemCarta.setOnClickListener {
                if (cartasSelecionadas < maxCartasSelecionadas) {
                    pontosJogadaAtual += cartas[index]
                    cartasSelecionadas++
                }

                if (cartasSelecionadas >= maxCartasSelecionadas) {
                    for (imagem in imagensCarta) {
                        imagem.isEnabled = false
                    }
                    btnEquipaA.isEnabled = true
                    btnEquipaB.isEnabled = true
                }
            }
        }

        btnEquipaA.setOnClickListener {
            pontosEquipaA += pontosJogadaAtual
            textViewEquipaA.text = "$nomeEquipeA: $pontosEquipaA"
            resetarJogada()
        }

        btnEquipaB.setOnClickListener {
            pontosEquipaB += pontosJogadaAtual
            textViewEquipaB.text = "$nomeEquipeB: $pontosEquipaB"
            resetarJogada()
        }

        btnTerminar.setOnClickListener {
            val intent = Intent(this, History::class.java)
            val vencedor: String
            if (pontosEquipaA > pontosEquipaB) {
                vencedor = "Equipa A"
            } else if (pontosEquipaB > pontosEquipaA) {
                vencedor = "Equipa B"
            } else {
                vencedor = "Empate"
            }

            intent.putExtra("pontosEquipaA", pontosEquipaA)
            intent.putExtra("pontosEquipaB", pontosEquipaB)
            intent.putExtra("vencedor", vencedor)
            startActivity(intent)
        }
    }

    private fun resetarJogada() {
        pontosJogadaAtual = 0
        cartasSelecionadas = 0
        val imagensCarta = listOf(imagemAs, imagemBisca, imagemRei, imagemValete, imagemDama)
        for (imagemCarta in imagensCarta) {
            imagemCarta.isEnabled = true
        }
    }
}
