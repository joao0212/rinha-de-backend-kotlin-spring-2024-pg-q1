package br.com.rinha.rinhabackend.cliente.controller

import br.com.rinha.rinhabackend.cliente.exception.ClienteNotFoundException
import br.com.rinha.rinhabackend.transacao.domain.ExtratoResponse
import br.com.rinha.rinhabackend.transacao.domain.Transacao
import br.com.rinha.rinhabackend.transacao.domain.TransacaoResponse
import br.com.rinha.rinhabackend.transacao.exception.TransacaoNaoPermitidaException
import br.com.rinha.rinhabackend.transacao.service.TransacaoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/clientes")
class ClienteController(
    val transacaoService: TransacaoService,
) {

    @PostMapping("/{id}/transacoes")
    suspend fun adicionarTransacao(
        @PathVariable("id") idCliente: Int,
        @Valid @RequestBody
        transacao: Transacao,
    ): ResponseEntity<TransacaoResponse> {
        return try {
            val cliente = transacaoService.validarTransacao(idCliente, transacao)
            ResponseEntity(TransacaoResponse(cliente?.limite, cliente?.saldo), HttpStatus.OK)
        } catch (e: TransacaoNaoPermitidaException) {
            ResponseEntity(TransacaoResponse(mensagemErro = "Transação não permitida"), HttpStatus.valueOf(422))
        } catch (e: ClienteNotFoundException) {
            ResponseEntity(TransacaoResponse(mensagemErro = "Cliente não encontrado"), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{id}/extrato")
    suspend fun buscarExtrato(
        @PathVariable("id") idCliente: Int,
    ): ResponseEntity<ExtratoResponse> {
        return try {
            val extratoResponse = transacaoService.extrato(idCliente)
            return ResponseEntity(extratoResponse, HttpStatus.OK)
        } catch (e: ClienteNotFoundException) {
            ResponseEntity(ExtratoResponse(mensagemErro = "Cliente não encontrado"), HttpStatus.NOT_FOUND)
        }
    }
}
