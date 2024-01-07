package com.example.suecacounter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {

    private lateinit var equipeA: String
    private lateinit var equipeB: String
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val startButton: Button = findViewById(R.id.startButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        startButton.setOnClickListener {
            showCustomDialog()
        }

        logoutButton.setOnClickListener {
            // Chama a função para fazer logout
            firebaseAuth.signOut()
            Toast.makeText(this, "Logout bem-sucedido", Toast.LENGTH_SHORT).show()

            // Redireciona o usuário de volta para a tela de login ou signup
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
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

                // Iniciar a CounterActivity passando os nomes das equipas
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
