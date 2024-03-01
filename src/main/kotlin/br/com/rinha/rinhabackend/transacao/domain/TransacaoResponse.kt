package br.com.rinha.rinhabackend.transacao.domain

import com.fasterxml.jackson.annotation.JsonInclude

data class TransacaoResponse(
    val limite: Int? = null,
    val saldo: Int? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val mensagemErro: String? = null
)