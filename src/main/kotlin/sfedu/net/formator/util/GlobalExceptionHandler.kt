package sfedu.net.formator.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(mapOf("message" to "Access denied"))
    }

//    @ExceptionHandler(AuthenticationException::class)
//    fun handleAuthException(ex: AuthenticationException): ResponseEntity<Map<String, String>> {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//            .body(mapOf("message" to "Authentication failed"))
//    }
}