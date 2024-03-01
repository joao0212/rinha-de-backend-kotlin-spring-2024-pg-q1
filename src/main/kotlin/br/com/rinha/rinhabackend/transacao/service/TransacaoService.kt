package br.com.rinha.rinhabackend.transacao.service

import br.com.rinha.rinhabackend.cliente.domain.Cliente
import br.com.rinha.rinhabackend.cliente.exception.ClienteNotFoundException
import br.com.rinha.rinhabackend.cliente.repository.ClienteRepository
import br.com.rinha.rinhabackend.transacao.domain.*
import br.com.rinha.rinhabackend.transacao.exception.TransacaoNaoPermitidaException
import br.com.rinha.rinhabackend.transacao.repository.TransacaoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.relational.core.sql.LockMode
import org.springframework.data.relational.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class TransacaoService(
    private val clienteRepository: ClienteRepository,
    private val transacaoRepository: TransacaoRepository,
) {

    suspend fun validarTransacao(clienteId: Int, transacao: Transacao): TransacaoResponse? {
        return coroutineScope {
            val cliente = async { buscarCliente(clienteId) }
            cliente.await()?.let {
                val clieteValidado = validarTipoTransacao(transacao, it)
                atualizar(clieteValidado, transacao)
                TransacaoResponse(clieteValidado.limite, clieteValidado.saldo)
            }
        }
    }

    @Transactional
    @Lock(LockMode.PESSIMISTIC_WRITE)
    private suspend fun atualizar(cliente: Cliente, transacao: Transacao) {
        clienteRepository.save(cliente)
        transacaoRepository.save(transacao.copy(idCliente = cliente.id, realizadaEm = Instant.now()))
    }

    private fun validarTipoTransacao(transacao: Transacao, cliente: Cliente): Cliente {
        val clienteAtualizado = when (transacao.tipo) {
            "c" -> realizarCredito(cliente, transacao)
            "d" -> realizarDebito(cliente, transacao)
            else -> throw TransacaoNaoPermitidaException()
        }
        return clienteAtualizado
    }

    fun realizarCredito(cliente: Cliente, transacao: Transacao): Cliente {
        cliente.saldo = cliente.saldo + transacao.valor.toInt()
        return cliente
    }

    fun realizarDebito(cliente: Cliente, transacao: Transacao): Cliente {
        if (cliente.saldo + cliente.limite > transacao.valor.toInt()) {
            cliente.saldo = cliente.saldo - transacao.valor.toInt()
        } else {
            throw TransacaoNaoPermitidaException()
        }
        return cliente
    }

    suspend fun extrato(idCliente: Int): ExtratoResponse? {
        return coroutineScope {
            val cliente = async { buscarCliente(idCliente) }
            cliente.await().let {
                transacaoRepository.findLast10(idCliente).let { transacoes ->
                    ExtratoResponse().apply {
                        this.saldo = SaldoResponse(it.saldo, LocalDateTime.now(), it?.limite)
                        this.ultimasTransacoes = transacoes.map { transacao -> TransacaoDTO(transacao.valor.toInt(), transacao.tipo, transacao.descricao, transacao.realizadaEm) }
                    }
                }
            }
        }
    }

    private suspend fun buscarCliente(clienteId: Int) = clienteRepository.findById(clienteId).orElseThrow(::ClienteNotFoundException)
}
