package com.example.suecacounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JogoAdapter(private var listaJogos: List<Jogo> = ArrayList()) : RecyclerView.Adapter<JogoAdapter.JogoViewHolder>() {

    inner class JogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val equipaA: TextView = itemView.findViewById(R.id.textViewEquipaA)
        val equipaB: TextView = itemView.findViewById(R.id.textViewEquipaB)
        val vencedor: TextView = itemView.findViewById(R.id.textViewVencedor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false)
        return JogoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        val jogoAtual = listaJogos[position]
        val pontuacaoEquipaA = "${jogoAtual.nomeEquipaA}: ${jogoAtual.pontosEquipaA}"
        val pontuacaoEquipaB = "${jogoAtual.nomeEquipaB}: ${jogoAtual.pontosEquipaB}"

        holder.equipaA.text = pontuacaoEquipaA
        holder.equipaB.text = pontuacaoEquipaB

        if (jogoAtual.vencedor == "Empate") {
            holder.vencedor.text = "Empate"
        } else {
            holder.vencedor.text = "Vencedor: ${jogoAtual.vencedor}"
        }
    }

    override fun getItemCount(): Int {
        return listaJogos.size
    }

    fun updateData(novaListaJogos: List<Jogo>) {
        listaJogos = novaListaJogos
        notifyDataSetChanged()
    }
}

