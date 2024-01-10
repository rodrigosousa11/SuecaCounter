package com.example.suecacounter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suecacounter.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class History : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    private lateinit var adapter: JogoAdapter
    private lateinit var ref: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonBack = binding.buttonBack

        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = binding.recyclerViewHistory
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
                jogosList.reverse()
                adapter.updateData(jogosList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", error.message)
            }
        })
    }
}
