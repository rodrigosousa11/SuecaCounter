package com.example.suecacounter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suecacounter.databinding.ItemJogoBinding
import java.text.SimpleDateFormat
import java.util.*

class JogoAdapter(private var listaJogos: List<Jogo> = ArrayList()) : RecyclerView.Adapter<JogoAdapter.JogoViewHolder>() {

    inner class JogoViewHolder(private val binding: ItemJogoBinding) : RecyclerView.ViewHolder(binding.root) {
        val equipeA: TextView = binding.textViewEquipaA
        val equipeB: TextView = binding.textViewEquipaB
        val vencedor: TextView = binding.textViewVencedor
        val data: TextView = binding.textViewData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJogoBinding.inflate(inflater, parent, false)
        return JogoViewHolder(binding)
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
