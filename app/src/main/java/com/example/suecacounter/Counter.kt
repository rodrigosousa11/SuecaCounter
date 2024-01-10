package com.example.suecacounter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.suecacounter.databinding.ActivityCounterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class Counter : AppCompatActivity() {
    private lateinit var binding: ActivityCounterBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonBack = binding.buttonBack

        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        userId = currentUser?.uid ?: ""

        ref = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("jogos")

        textViewEquipaA = binding.textViewEquipaA
        textViewEquipaB = binding.textViewEquipaB
        btnEquipaA = binding.buttonWinnerA
        btnEquipaB = binding.buttonWinnerB
        btnTerminar = binding.button1

        imagemAs = binding.imagemas
        imagemBisca = binding.imagemBisca
        imagemRei = binding.imagemRei
        imagemValete = binding.imagemValete
        imagemDama = binding.imagemDama

        val nomeEquipaA = intent.getStringExtra("nomeEquipaA") ?: ""
        val nomeEquipaB = intent.getStringExtra("nomeEquipaB") ?: ""

        val textViewNomeEquipeA = binding.textViewEquipaA
        val textViewNomeEquipeB = binding.textViewEquipaB
        textViewNomeEquipeA.text = "$nomeEquipaA: $pontosEquipaA"
        textViewNomeEquipeB.text = "$nomeEquipaB: $pontosEquipaB"

        val btnEquipaA = binding.buttonWinnerA
        val btnEquipaB = binding.buttonWinnerB
        btnEquipaA.text = nomeEquipaA
        btnEquipaB.text = nomeEquipaB

        val cartas = listOf(11, 10, 4, 3, 2)

        val cartasNomes = listOf("Ás", "Bisca", "Rei", "Valete", "Dama")
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

                        showToast("Selecionou a carta $nomeCarta ${cartasContagem[nomeCarta]}x.")
                        Log.d("DEBUG", "cartasSelecionadas: $cartasSelecionadas")
                    } else {
                        showToast("Já foram jogadas todas as cartas $nomeCarta!")
                    }
                } else {
                    showToast("Já foram jogadas todas as cartas!")
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
