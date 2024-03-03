package br.com.rinha.rinhabackend.transacao.domain

import java.sql.Timestamp

data class TransacaoDTO(
    val valor: Int,
    val tipo: String,
    val descricao: String,
    val realizadaEm: Timestamp? = null,
)