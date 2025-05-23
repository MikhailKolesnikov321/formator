package sfedu.net.formator.application.security.rest

data class RegisterRequest(
    val username: String,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String
)

data class AuthResponse(val token: String)