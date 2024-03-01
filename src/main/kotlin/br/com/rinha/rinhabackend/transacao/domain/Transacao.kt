package br.com.rinha.rinhabackend.transacao.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.*
import org.springframework.data.annotation.Id
import java.time.Instant

data class Transacao(
    @Id
    @JsonIgnore
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
    val idCliente: Int? = null,
    val realizadaEm: Instant? = null
)