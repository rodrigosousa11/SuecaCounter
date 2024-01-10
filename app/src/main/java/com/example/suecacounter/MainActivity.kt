package com.example.suecacounter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.suecacounter.databinding.ActivityMainBinding
import com.example.suecacounter.databinding.DialogInputNamesBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var equipaA: String
    private lateinit var equipaB: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val startButton = binding.startButton
        val logoutButton = binding.logoutButton
        val historyButton = binding.historyButton

        startButton.setOnClickListener {
            showCustomDialog()
        }

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logout bem-sucedido", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, History::class.java)
            startActivity(intent)
        }
    }

    private fun showCustomDialog() {
        val dialogBinding = DialogInputNamesBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)
            .setPositiveButton("OK") { dialog, _ ->
                equipaA = dialogBinding.editTextEquipaA.text.toString().takeIf { it.isNotBlank() } ?: "Equipa A"
                equipaB = dialogBinding.editTextEquipaB.text.toString().takeIf { it.isNotBlank() } ?: "Equipa B"

                if (equipaA.length > 22 || equipaB.length > 22) {
                    Toast.makeText(this, "Por favor insira um nome com menos de 23 caracteres.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, Counter::class.java)
                    intent.putExtra("nomeEquipaA", equipaA)
                    intent.putExtra("nomeEquipaB", equipaB)
                    startActivity(intent)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
