package sfedu.net.formator.util

import arrow.core.Nel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface ValidationError {
    val message: String
}

class SimpleValidationError(
    override val message: String,
) : ValidationError

fun restBusinessError(body: Any): ResponseEntity<*> =
    ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body)

fun Nel<ValidationError>.toInvalidParamsBadRequest(): ResponseEntity<*> =
    ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(mapOf("errors" to this.map { it.message }))