package br.com.rinha.rinhabackend.cliente.controller.exceptionhandler

import jakarta.validation.ValidationException
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ExceptionHandlerAPI {

    @ExceptionHandler(
        ValidationException::class,
        MethodArgumentNotValidException::class,
        HttpMessageNotReadableException::class
    )
    fun handleDomainException(ex: Exception, req: WebRequest): ResponseEntity<String> {
        return status(422).body(ex.message)
    }
}