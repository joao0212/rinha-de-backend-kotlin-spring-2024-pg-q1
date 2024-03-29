package br.com.rinha.rinhabackend.transacao.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table
data class Transacao(
    @Id
    val id: Int,

    @get:NotNull
    @get:PositiveOrZero
    @get:Digits(integer = 1000000000, fraction = 0)
    val valor: Double,

    @get:NotNull
    @get:NotBlank
    @get:NotEmpty
    val tipo: String,

    @get:Size(min = 1, max = 10)
    @get:NotNull
    @get:NotBlank
    @get:NotEmpty
    val descricao: String,
    @JsonIgnore
    @Column("id_cliente")
    val idCliente: Int? = null,
    val realizadaEm: Timestamp? = null
)