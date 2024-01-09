package com.example.suecacounter

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class History : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JogoAdapter
    private lateinit var ref: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JogoAdapter()
        recyclerView.adapter = adapter

        val firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        ref = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(currentUser.uid)
            .child("jogos")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jogosList = mutableListOf<Jogo>()
                for (gameSnapshot in snapshot.children) {
                    val jogo = gameSnapshot.getValue(Jogo::class.java)
                    jogo?.let { jogosList.add(it) }
                }
                adapter.updateData(jogosList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Em caso de erro na leitura do banco de dados
            }
        })
    }
}
