package sfedu.net.formator.application.security.rest

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val userId: String,
    val token: String
)