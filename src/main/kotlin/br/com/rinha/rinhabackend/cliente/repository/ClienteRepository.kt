package br.com.rinha.rinhabackend.cliente.repository

import br.com.rinha.rinhabackend.cliente.domain.Cliente
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClienteRepository : CrudRepository<Cliente, Int> {

    @Query("SELECT * FROM cliente WHERE id = :clienteId FOR UPDATE")
    fun buscaClientePorLockUpdate(@Param("clienteId") clienteId: Int): Optional<Cliente>

    @Query("UPDATE cliente SET saldo = :saldo WHERE id = :clienteId RETURNING *")
    fun update(@Param("saldo") saldo: Int, @Param("clienteId") clienteId: Int): Cliente
}