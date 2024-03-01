package br.com.rinha.rinhabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["br.com.rinha.rinhabackend"])
class RinhaBackendApplication

fun main(args: Array<String>) {
    runApplication<RinhaBackendApplication>(*args)
}
