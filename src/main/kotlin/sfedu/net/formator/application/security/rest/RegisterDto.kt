package sfedu.net.formator.application.security.rest

import java.util.UUID

data class RegisterRequest(
    val username: String,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String
)

data class RegisterResponse(
    val userId: UUID,
    val token: String
)