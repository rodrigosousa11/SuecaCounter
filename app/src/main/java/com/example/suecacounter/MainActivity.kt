package com.example.suecacounter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {

    private lateinit var equipaA: String
    private lateinit var equipaB: String
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val startButton: LinearLayout = findViewById(R.id.startButton)
        val logoutButton: LinearLayout = findViewById(R.id.logoutButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)

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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_names, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val editTextEquipeA: EditText = dialogView.findViewById(R.id.editTextEquipaA)
                val editTextEquipeB: EditText = dialogView.findViewById(R.id.editTextEquipaB)

                equipaA = editTextEquipeA.text.toString().takeIf { it.isNotBlank() } ?: "Equipa A"
                equipaB = editTextEquipeB.text.toString().takeIf { it.isNotBlank() } ?: "Equipa B"

                val intent = Intent(this, Counter::class.java)
                intent.putExtra("nomeEquipaA", equipaA)
                intent.putExtra("nomeEquipaB", equipaB)
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
