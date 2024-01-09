package com.example.suecacounter

import java.text.SimpleDateFormat
import java.util.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JogoAdapter(private var listaJogos: List<Jogo> = ArrayList()) : RecyclerView.Adapter<JogoAdapter.JogoViewHolder>() {

    inner class JogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val equipeA: TextView = itemView.findViewById(R.id.textViewEquipaA)
        val equipeB: TextView = itemView.findViewById(R.id.textViewEquipaB)
        val vencedor: TextView = itemView.findViewById(R.id.textViewVencedor)
        val data: TextView = itemView.findViewById(R.id.textViewData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false)
        return JogoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        val jogoAtual = listaJogos[position]
        val pontuacaoEquipaA = "${jogoAtual.nomeEquipaA}: ${jogoAtual.pontosEquipaA}"
        val pontuacaoEquipaB = "${jogoAtual.nomeEquipaB}: ${jogoAtual.pontosEquipaB}"

        holder.equipeA.text = pontuacaoEquipaA
        holder.equipeB.text = pontuacaoEquipaB

        if (jogoAtual.vencedor == "Empate") {
            holder.vencedor.text = "Empate"
        } else {
            holder.vencedor.text = "Vencedor: ${jogoAtual.vencedor}"
        }

        val dataJogo = jogoAtual.data
        val dataFormatada = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(Date(dataJogo))
        holder.data.text = "Data: $dataFormatada"
    }


    override fun getItemCount(): Int {
        return listaJogos.size
    }

    fun updateData(novaListaJogos: List<Jogo>) {
        listaJogos = novaListaJogos
        notifyDataSetChanged()
    }
}

