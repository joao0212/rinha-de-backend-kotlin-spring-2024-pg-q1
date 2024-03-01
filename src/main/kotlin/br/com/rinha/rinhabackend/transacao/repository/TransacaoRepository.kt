package br.com.rinha.rinhabackend.transacao.repository

import br.com.rinha.rinhabackend.transacao.domain.Transacao
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TransacaoRepository : CrudRepository<Transacao, Int> {

    @Query("select * from transacao where id_cliente = :clienteId order by realizada_em desc limit 10")
    fun findLast10(@Param("clienteId") clienteId: Int): List<Transacao>
}
