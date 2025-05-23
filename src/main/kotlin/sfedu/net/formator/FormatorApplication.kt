package sfedu.net.formator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import sfedu.net.formator.application.security.JwtTokenProvider
import sfedu.net.formator.domain.Role
import sfedu.net.formator.util.runLiquibaseMigrations
import java.util.UUID

@SpringBootApplication
class FormatorApplication

fun main(args: Array<String>) {
    runLiquibaseMigrations()
    runApplication<FormatorApplication>(*args)
}
