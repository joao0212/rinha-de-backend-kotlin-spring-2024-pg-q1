package br.com.rinha.rinhabackend.cliente.repository

import br.com.rinha.rinhabackend.cliente.domain.Cliente
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ClienteRepository : CrudRepository<Cliente, Int>