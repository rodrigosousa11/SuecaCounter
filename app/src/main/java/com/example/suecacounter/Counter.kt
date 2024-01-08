package com.example.suecacounter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

    private lateinit var ref: DatabaseReference
    private lateinit var userId: String

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        userId = currentUser?.uid ?: ""

        ref = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("jogos")

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

        val nomeEquipaA = intent.getStringExtra("nomeEquipaA") ?: ""
        val nomeEquipaB = intent.getStringExtra("nomeEquipaB") ?: ""

        val textViewNomeEquipeA = findViewById<TextView>(R.id.textViewEquipaA)
        val textViewNomeEquipeB = findViewById<TextView>(R.id.textViewEquipaB)
        textViewNomeEquipeA.text = "$nomeEquipaA: $pontosEquipaA"
        textViewNomeEquipeB.text = "$nomeEquipaB: $pontosEquipaB"

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
            textViewEquipaA.text = "$nomeEquipaA: $pontosEquipaA"
            resetarJogada()
        }

        btnEquipaB.setOnClickListener {
            pontosEquipaB += pontosJogadaAtual
            textViewEquipaB.text = "$nomeEquipaB: $pontosEquipaB"
            resetarJogada()
        }

        btnTerminar.setOnClickListener {
            val vencedor: String
            if (pontosEquipaA > pontosEquipaB) {
                vencedor = "$nomeEquipaA"
            } else if (pontosEquipaB > pontosEquipaA) {
                vencedor = "$nomeEquipaB"
            } else {
                vencedor = "Empate"
            }
            salvarDetalhesDoJogo(nomeEquipaA, nomeEquipaB, pontosEquipaA, pontosEquipaB, vencedor)

            // Crie um Intent para iniciar a nova atividade (Quem_ganhou)
            val intent = Intent(this, Ganhou::class.java)

            // Adicione o valor do vencedor como um extra no Intent
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

    private fun salvarDetalhesDoJogo(
        nomeEquipaA: String,
        nomeEquipaB: String,
        pontosEquipaA: Int,
        pontosEquipaB: Int,
        vencedor: String
    ) {
        val novoJogoRef = ref.push()
        val jogo = HashMap<String, Any>()
        jogo["nomeEquipaA"] = nomeEquipaA
        jogo["nomeEquipaB"] = nomeEquipaB
        jogo["pontosEquipaA"] = pontosEquipaA
        jogo["pontosEquipaB"] = pontosEquipaB
        jogo["vencedor"] = vencedor

        novoJogoRef.setValue(jogo)
            .addOnSuccessListener {
                Log.d("TAG", "Dados inseridos com sucesso")
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Erro ao inserir os dados: ${e.message}")
            }
    }
}
