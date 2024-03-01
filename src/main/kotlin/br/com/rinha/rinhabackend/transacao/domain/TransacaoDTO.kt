package br.com.rinha.rinhabackend.transacao.domain

import java.time.Instant

data class TransacaoDTO(
    val valor: Int,
    val tipo: String,
    val descricao: String,
    val realizadaEm: Instant? = null,
)