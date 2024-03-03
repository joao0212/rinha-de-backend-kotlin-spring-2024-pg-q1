package br.com.rinha.rinhabackend.transacao.repository

import br.com.rinha.rinhabackend.transacao.domain.Transacao
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class TransacaoRepository(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    val transacaoRowMapper: TransacaoRowMapper
) {

    fun findLast10(idCliente: Int): List<Transacao> {
        return namedParameterJdbcTemplate.queryForStream("""
            SELECT * FROM transacao WHERE id_cliente = :clienteId ORDER BY realizada_em DESC LIMIT 10
        """.trimIndent(), mapOf("clienteId" to idCliente), transacaoRowMapper).toList()
    }

    fun save(transacao: Transacao) {
        namedParameterJdbcTemplate.update("""
            INSERT INTO transacao (valor, tipo, descricao, id_cliente, realizada_em) 
            VALUES (:valor, :tipo, :descricao, :idCliente, :realizadaEm)
        """.trimIndent(),
        mapOf(
            "valor" to transacao.valor,
            "tipo" to transacao.tipo,
            "descricao" to transacao.descricao,
            "idCliente" to transacao.idCliente,
            "realizadaEm" to transacao.realizadaEm)
        )
    }
}

@Component
class TransacaoRowMapper : RowMapper<Transacao> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Transacao {
        return Transacao(
            rs.getDouble("valor"),
            rs.getString("tipo"),
            rs.getString("descricao"),
            rs.getInt("id_cliente"),
            rs.getTimestamp("realizada_em")
        )
    }
}
