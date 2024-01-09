package com.example.suecacounter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

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
        btnEquipaA = findViewById(R.id.button3)
        btnEquipaB = findViewById(R.id.button2)
        btnTerminar = findViewById(R.id.button1)

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

        val cartasNomes = listOf("As", "Bisca", "Rei", "Valete", "Dama")
        val cartasContagem = mutableMapOf<String, Int>()

        val imagensCarta = listOf(imagemAs, imagemBisca, imagemRei, imagemValete, imagemDama)
        val cartasLimites = mutableMapOf<String, Int>()

        for ((index, imagemCarta) in imagensCarta.withIndex()) {
            val nomeCarta = cartasNomes[index]
            cartasContagem[nomeCarta] = 0
            cartasLimites[nomeCarta] = 4

            imagemCarta.setOnClickListener {
                if (cartasSelecionadas < maxCartasSelecionadas) {
                    if (cartasContagem[nomeCarta]!! < cartasLimites[nomeCarta]!!) {
                        pontosJogadaAtual += cartas[index]
                        cartasSelecionadas++

                        val contagemAtual = cartasContagem.getValue(nomeCarta)
                        cartasContagem[nomeCarta] = contagemAtual + 1

                        showToast("Você selecionou o $nomeCarta ${cartasContagem[nomeCarta]}x")
                        Log.d("DEBUG", "cartasSelecionadas: $cartasSelecionadas")
                    } else {
                        showToast("Não pode selecionar mais $nomeCarta")
                    }
                } else {
                    showToast("Você atingiu o limite total de seleções")
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

            val intent = Intent(this, Ganhou::class.java)

            intent.putExtra("vencedor", vencedor)

            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        jogo["data"] = ServerValue.TIMESTAMP

        novoJogoRef.setValue(jogo)
            .addOnSuccessListener {
                Log.d("TAG", "Dados inseridos com sucesso")
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Erro ao inserir os dados: ${e.message}")
            }
    }
}
