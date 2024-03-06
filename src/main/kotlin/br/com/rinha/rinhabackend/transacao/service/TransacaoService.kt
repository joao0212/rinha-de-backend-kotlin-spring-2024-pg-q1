package br.com.rinha.rinhabackend.transacao.service

import br.com.rinha.rinhabackend.cliente.exception.ClienteNotFoundException
import br.com.rinha.rinhabackend.cliente.repository.ClienteRepository
import br.com.rinha.rinhabackend.transacao.domain.*
import br.com.rinha.rinhabackend.transacao.exception.TransacaoNaoPermitidaException
import br.com.rinha.rinhabackend.transacao.repository.TransacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class TransacaoService(
    private val clienteRepository: ClienteRepository,
    private val transacaoRepository: TransacaoRepository,
) {

    @Transactional
    fun validarTransacao(clienteId: Int, transacao: Transacao): TransacaoResponse {
        val cliente = clienteRepository.buscaClientePorLockUpdate(clienteId).orElseThrow(::ClienteNotFoundException)
        when (transacao.tipo) {
            "c" -> cliente.saldo = cliente.saldo + transacao.valor.toInt()
            "d" -> if (cliente.saldo + cliente.limite > transacao.valor.toInt()) {
                cliente.saldo = cliente.saldo - transacao.valor.toInt()
            } else {
                throw TransacaoNaoPermitidaException()
            }
            else -> {
                throw TransacaoNaoPermitidaException()
            }
        }
        clienteRepository.update(cliente.saldo, cliente.id)
        transacaoRepository.save(transacao.copy(idCliente = cliente.id, realizadaEm = Timestamp(System.currentTimeMillis())))
        return TransacaoResponse(cliente.limite, cliente.saldo)
    }

    fun extrato(idCliente: Int): ExtratoResponse {
        val cliente = clienteRepository.findById(idCliente).orElseThrow(::ClienteNotFoundException)
        val transacoes = transacaoRepository.findLast10(idCliente).map { transacao -> TransacaoDTO(transacao.valor.toInt(), transacao.tipo, transacao.descricao, transacao.realizadaEm) }
        return if (transacoes.isNotEmpty()) {
            ExtratoResponse().apply {
                this.saldo = SaldoResponse(cliente.saldo, LocalDateTime.now(), cliente.limite)
                this.ultimasTransacoes = transacoes
            }
        } else {
            ExtratoResponse().apply {
                this.saldo = SaldoResponse(cliente.saldo, LocalDateTime.now(), cliente.limite)
            }
        }
    }
}
