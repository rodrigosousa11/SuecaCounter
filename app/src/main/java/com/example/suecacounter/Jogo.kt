package com.example.suecacounter

data class Jogo(
    var nomeEquipaA: String = "",
    var nomeEquipaB: String = "",
    var pontosEquipaA: Int = 0,
    var pontosEquipaB: Int = 0,
    var vencedor: String = "",
    var data: Long = 0
)
