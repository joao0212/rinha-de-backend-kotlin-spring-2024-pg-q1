package br.com.rinha.rinhabackend.cliente.domain

import org.springframework.data.annotation.Id

data class Cliente(
    @Id
    val id: Int,
    val limite: Int,
    var saldo: Int
)