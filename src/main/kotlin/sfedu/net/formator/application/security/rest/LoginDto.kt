package sfedu.net.formator.application.security.rest

import java.util.UUID

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val userId: UUID,
    val token: String
)