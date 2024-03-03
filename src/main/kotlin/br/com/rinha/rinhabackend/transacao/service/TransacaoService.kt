package br.com.rinha.rinhabackend.transacao.service

import br.com.rinha.rinhabackend.cliente.domain.Cliente
import br.com.rinha.rinhabackend.cliente.exception.ClienteNotFoundException
import br.com.rinha.rinhabackend.cliente.repository.ClienteRepository
import br.com.rinha.rinhabackend.transacao.domain.*
import br.com.rinha.rinhabackend.transacao.exception.TransacaoNaoPermitidaException
import br.com.rinha.rinhabackend.transacao.repository.TransacaoRepository
import org.springframework.data.relational.core.sql.LockMode
import org.springframework.data.relational.repository.Lock
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
        return clienteRepository.buscaClientePorLockUpdate(clienteId).orElseThrow(::ClienteNotFoundException).let {
            val clienteValidado: Cliente = when (transacao.tipo) {
                "c" -> it.copy(saldo = it.saldo + transacao.valor.toInt())
                "d" -> if (it.saldo + it.limite > transacao.valor.toInt()) {
                    it.copy(saldo = it.saldo - transacao.valor.toInt())
                } else {
                    throw TransacaoNaoPermitidaException()
                }
                else -> {
                    throw TransacaoNaoPermitidaException()
                }
            }
            clienteRepository.update(clienteValidado.saldo, clienteValidado.id)
            val transacaoAtualizada = transacao.copy(idCliente = clienteValidado.id, realizadaEm = Timestamp(System.currentTimeMillis()))
            transacaoRepository.save(transacaoAtualizada)
            TransacaoResponse(clienteValidado.limite, clienteValidado.saldo)
        }
    }

    @Transactional
    @Lock(LockMode.PESSIMISTIC_READ)
    fun extrato(idCliente: Int): ExtratoResponse? {
        val cliente = clienteRepository.findById(idCliente).orElseThrow(::ClienteNotFoundException)
        return transacaoRepository.findLast10(idCliente).let { transacoes ->
            ExtratoResponse().apply {
                this.saldo = SaldoResponse(cliente.saldo, LocalDateTime.now(), cliente.limite)
                this.ultimasTransacoes = transacoes.map { transacao -> TransacaoDTO(transacao.valor.toInt(), transacao.tipo, transacao.descricao, transacao.realizadaEm) }
            }
        }
    }
}
